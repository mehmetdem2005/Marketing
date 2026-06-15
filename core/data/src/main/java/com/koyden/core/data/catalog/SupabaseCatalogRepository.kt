package com.koyden.core.data.catalog

import com.koyden.core.common.result.DataResult
import com.koyden.core.data.auth.toAppError
import com.koyden.core.domain.catalog.Category
import com.koyden.core.domain.catalog.CatalogRepository
import com.koyden.core.domain.catalog.Product
import com.koyden.core.domain.catalog.ProductQuery
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [CatalogRepository]'nin Supabase Postgrest implementasyonu (public read; RLS şemada).
 * Ürün sorgusu mağaza adı + görselleri gömülü çeker (tek istek — N+1 yok).
 */
@Singleton
class SupabaseCatalogRepository @Inject constructor(
    private val client: SupabaseClient,
) : CatalogRepository {

    private val db get() = client.postgrest

    override suspend fun getCategories(): DataResult<List<Category>> = runResult {
        db.from(CATEGORIES).select {
            order("sort_order", Order.ASCENDING)
        }.decodeList<CategoryDto>().map { it.toDomain() }
    }

    override suspend fun getProducts(query: ProductQuery): DataResult<List<Product>> = runResult {
        db.from(PRODUCTS).select(Columns.raw(PRODUCT_COLUMNS)) {
            filter {
                eq("is_active", true)
                query.categoryId?.let { eq("category_id", it) }
                query.search?.takeIf { it.isNotBlank() }?.let { ilike("name", "%$it%") }
            }
            order("created_at", Order.DESCENDING)
        }.decodeList<ProductRowDto>().map { it.toDomain() }
    }

    override suspend fun getProduct(id: String): DataResult<Product> = runResult {
        db.from(PRODUCTS).select(Columns.raw(PRODUCT_COLUMNS)) {
            filter { eq("id", id) }
        }.decodeSingle<ProductRowDto>().toDomain()
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
        const val CATEGORIES = "categories"
        const val PRODUCTS = "products"
    }
}
