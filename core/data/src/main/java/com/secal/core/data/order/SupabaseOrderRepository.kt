package com.secal.core.data.order

import com.secal.core.common.result.DataResult
import com.secal.core.data.auth.toAppError
import com.secal.core.domain.order.Order
import com.secal.core.domain.order.OrderItem
import com.secal.core.domain.order.OrderRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order as PgOrder
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
internal data class OrderItemRowDto(
    val id: String,
    @SerialName("product_name") val productName: String,
    @SerialName("unit_price_minor") val unitPriceMinor: Long,
    val quantity: Int,
)

@Serializable
internal data class OrderRowDto(
    val id: String,
    @SerialName("total_minor") val totalMinor: Long,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("order_items") val orderItems: List<OrderItemRowDto> = emptyList(),
)

private fun OrderItemRowDto.toDomain() = OrderItem(
    id = id, productName = productName, unitPriceMinor = unitPriceMinor, quantity = quantity,
)

private fun OrderRowDto.toDomain() = Order(
    id = id,
    totalMinor = totalMinor,
    status = status,
    createdAt = createdAt,
    items = orderItems.map { it.toDomain() },
)

@Singleton
class SupabaseOrderRepository @Inject constructor(
    private val client: SupabaseClient,
) : OrderRepository {

    private val db get() = client.postgrest

    override suspend fun placeOrder(): DataResult<String> = runResult {
        db.rpc("place_order").decodeAs<String>()
    }

    override suspend fun getMyOrders(): DataResult<List<Order>> = runResult {
        db.from(ORDERS).select(Columns.raw(ORDER_COLUMNS)) {
            order("created_at", PgOrder.DESCENDING)
        }.decodeList<OrderRowDto>().map { it.toDomain() }
    }

    override suspend fun getOrder(orderId: String): DataResult<Order> = runResult {
        db.from(ORDERS).select(Columns.raw(ORDER_DETAIL_COLUMNS)) {
            filter { eq("id", orderId) }
        }.decodeSingle<OrderRowDto>().toDomain()
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
        const val ORDERS = "orders"
        const val ORDER_COLUMNS = "id,total_minor,status,created_at"
        const val ORDER_DETAIL_COLUMNS =
            "id,total_minor,status,created_at,order_items(id,product_name,unit_price_minor,quantity)"
    }
}
