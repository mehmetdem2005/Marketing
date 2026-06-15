package com.koyden.core.domain.catalog.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.catalog.CatalogRepository
import com.koyden.core.domain.catalog.Product
import com.koyden.core.domain.catalog.ProductQuery
import javax.inject.Inject

/** Aktif ürünleri (kategori/arama filtresiyle) getirir. */
class GetProductsUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(query: ProductQuery = ProductQuery()): DataResult<List<Product>> =
        repository.getProducts(query)
}
