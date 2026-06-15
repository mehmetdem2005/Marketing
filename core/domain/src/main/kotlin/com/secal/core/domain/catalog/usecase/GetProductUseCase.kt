package com.secal.core.domain.catalog.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.catalog.CatalogRepository
import com.secal.core.domain.catalog.Product
import javax.inject.Inject

/** Tek ürün detayını (görseller + mağaza) getirir. */
class GetProductUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(id: String): DataResult<Product> = repository.getProduct(id)
}
