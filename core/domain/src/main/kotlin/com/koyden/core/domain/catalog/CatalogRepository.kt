package com.koyden.core.domain.catalog

import com.koyden.core.common.result.DataResult

/**
 * Katalog **portu** (domain). Data katmanı (Supabase Postgrest, public read / RLS) implement eder.
 * Keşif/arama/detay salt-okunurdur; satıcı yazma akışı ayrı port (Faz: seller).
 */
interface CatalogRepository {
    /** Tüm kategoriler (sort_order'a göre). */
    suspend fun getCategories(): DataResult<List<Category>>

    /** Aktif ürünler; [query] ile kategori/arama filtresi. */
    suspend fun getProducts(query: ProductQuery = ProductQuery()): DataResult<List<Product>>

    /** Tek ürün (görseller + mağaza adı dahil). */
    suspend fun getProduct(id: String): DataResult<Product>
}
