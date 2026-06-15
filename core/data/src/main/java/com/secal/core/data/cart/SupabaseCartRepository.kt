package com.secal.core.data.cart

import com.secal.core.common.result.DataResult
import com.secal.core.data.auth.toAppError
import com.secal.core.data.catalog.PRODUCT_COLUMNS
import com.secal.core.data.catalog.ProductRowDto
import com.secal.core.data.catalog.toDomain
import com.secal.core.domain.cart.CartItem
import com.secal.core.domain.cart.CartRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
internal data class CartItemRowDto(
    val id: String,
    val quantity: Int,
    val products: ProductRowDto,
)

@Serializable
internal data class AddToCartParams(
    @SerialName("p_product_id") val productId: String,
    @SerialName("p_qty") val qty: Int,
)

/** [CartRepository] Supabase implementasyonu. RLS sepeti sahibine kısıtlar. */
@Singleton
class SupabaseCartRepository @Inject constructor(
    private val client: SupabaseClient,
) : CartRepository {

    private val db get() = client.postgrest

    override suspend fun getCart(): DataResult<List<CartItem>> = runResult {
        db.from(CART_ITEMS).select(Columns.raw("id,quantity,products($PRODUCT_COLUMNS)")) {
            order("created_at", Order.ASCENDING)
        }.decodeList<CartItemRowDto>().map { row ->
            CartItem(id = row.id, product = row.products.toDomain(), quantity = row.quantity)
        }
    }

    override suspend fun addToCart(productId: String, quantity: Int): DataResult<Unit> = runResult {
        db.rpc("add_to_cart", AddToCartParams(productId = productId, qty = quantity))
        Unit
    }

    override suspend fun updateQuantity(itemId: String, quantity: Int): DataResult<Unit> = runResult {
        db.from(CART_ITEMS).update({ set("quantity", quantity) }) {
            filter { eq("id", itemId) }
        }
        Unit
    }

    override suspend fun removeItem(itemId: String): DataResult<Unit> = runResult {
        db.from(CART_ITEMS).delete { filter { eq("id", itemId) } }
        Unit
    }

    private inline fun <T> runResult(block: () -> T): DataResult<T> =
        try {
            DataResult.Success(block())
        } catch (c: CancellationException) {
            throw c
        } catch (t: Throwable) {
            DataResult.Failure(t.toAppError())
        }

    private companion object {
        const val CART_ITEMS = "cart_items"
    }
}
