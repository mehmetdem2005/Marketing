package com.secal.core.domain.catalog.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.catalog.Category
import com.secal.core.domain.catalog.CatalogRepository
import javax.inject.Inject

/** Kategorileri getirir (keşif filtresi için). */
class GetCategoriesUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): DataResult<List<Category>> = repository.getCategories()
}
