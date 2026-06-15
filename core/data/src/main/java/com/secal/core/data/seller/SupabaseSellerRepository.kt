package com.secal.core.data.seller

import com.secal.core.common.result.DataResult
import com.secal.core.data.auth.toAppError
import com.secal.core.data.catalog.PRODUCT_COLUMNS
import com.secal.core.data.catalog.ProductRowDto
import com.secal.core.data.catalog.toDomain
import com.secal.core.domain.catalog.Product
import com.secal.core.domain.catalog.Store
import com.secal.core.domain.seller.NewProduct
import com.secal.core.domain.seller.SellerRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CancellationException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [SellerRepository]'nin Supabase implementasyonu. Yazma işlemleri RLS ile mağaza sahibine
 * kısıtlıdır (owns_store). Görseller `product-images` Storage bucket'ına yüklenir.
 */
@Singleton
class SupabaseSellerRepository @Inject constructor(
    private val client: SupabaseClient,
) : SellerRepository {

    private val db get() = client.postgrest

    override suspend fun getMyStore(): DataResult<Store?> = runResult {
        db.from(STORES).select {
            filter { eq("owner_id", currentUserId()) }
        }.decodeSingleOrNull<StoreRowDto>()?.toDomain()
    }

    override suspend fun createStore(
        name: String,
        city: String?,
        description: String?,
        businessRegistrationNo: String?,
    ): DataResult<Store> = runResult {
        val payload = StoreInsertDto(
            ownerId = currentUserId(),
            name = name.trim(),
            slug = slugify(name),
            city = city?.trim()?.ifBlank { null },
            description = description?.trim()?.ifBlank { null },
            businessRegistrationNo = businessRegistrationNo?.trim()?.ifBlank { null },
        )
        db.from(STORES).insert(payload) { select() }.decodeSingle<StoreRowDto>().toDomain()
    }

    override suspend fun getMyProducts(storeId: String): DataResult<List<Product>> = runResult {
        db.from(PRODUCTS).select(Columns.raw(PRODUCT_COLUMNS)) {
            filter { eq("store_id", storeId) }
            order("created_at", Order.DESCENDING)
        }.decodeList<ProductRowDto>().map { it.toDomain() }
    }

    override suspend fun createProduct(draft: NewProduct): DataResult<Product> = runResult {
        val payload = ProductInsertDto(
            storeId = draft.storeId,
            name = draft.name.trim(),
            slug = slugify(draft.name),
            priceMinor = draft.priceMinor,
            stock = draft.stock,
            unit = draft.unit?.trim()?.ifBlank { null },
            categoryId = draft.categoryId,
            description = draft.description?.trim()?.ifBlank { null },
        )
        val created = db.from(PRODUCTS).insert(payload) { select() }.decodeSingle<ProductRowDto>()
        draft.imageUrl?.takeIf { it.isNotBlank() }?.let { url ->
            db.from(PRODUCT_IMAGES).insert(ProductImageInsertDto(productId = created.id, url = url))
        }
        // Görseli de içeren tam kaydı geri oku.
        db.from(PRODUCTS).select(Columns.raw(PRODUCT_COLUMNS)) {
            filter { eq("id", created.id) }
        }.decodeSingle<ProductRowDto>().toDomain()
    }

    override suspend fun uploadProductImage(
        storeId: String,
        bytes: ByteArray,
        extension: String,
    ): DataResult<String> = runResult {
        val path = "stores/$storeId/${UUID.randomUUID()}.${extension.ifBlank { "jpg" }}"
        val bucket = client.storage.from(BUCKET)
        bucket.upload(path, bytes) { upsert = true }
        bucket.publicUrl(path)
    }

    private fun currentUserId(): String =
        client.auth.currentUserOrNull()?.id ?: error("Oturum bulunamadı")

    private fun slugify(input: String): String {
        val mapped = input.lowercase()
            .replace('ç', 'c').replace('ğ', 'g').replace('ı', 'i')
            .replace('ö', 'o').replace('ş', 's').replace('ü', 'u')
        val base = mapped.replace(Regex("[^a-z0-9]+"), "-").trim('-').take(40)
        val suffix = UUID.randomUUID().toString().take(6)
        return if (base.isBlank()) suffix else "$base-$suffix"
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
        const val STORES = "stores"
        const val PRODUCTS = "products"
        const val PRODUCT_IMAGES = "product_images"
        const val BUCKET = "product-images"
    }
}
