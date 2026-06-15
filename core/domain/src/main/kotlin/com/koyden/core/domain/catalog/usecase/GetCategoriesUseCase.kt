package com.koyden.core.domain.catalog.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.catalog.Category
import com.koyden.core.domain.catalog.CatalogRepository
import javax.inject.Inject

/** Kategorileri getirir (keşif filtresi için). */
class GetCategoriesUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): DataResult<List<Category>> = repository.getCategories()
}
