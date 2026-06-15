package com.koyden.core.domain.catalog.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.catalog.CatalogRepository
import com.koyden.core.domain.catalog.Product
import javax.inject.Inject

/** Tek ürün detayını (görseller + mağaza) getirir. */
class GetProductUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(id: String): DataResult<Product> = repository.getProduct(id)
}
