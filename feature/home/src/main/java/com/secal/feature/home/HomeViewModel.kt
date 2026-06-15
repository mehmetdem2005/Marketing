package com.secal.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secal.core.common.result.DataResult
import com.secal.core.domain.catalog.Category
import com.secal.core.domain.catalog.Product
import com.secal.core.domain.catalog.ProductQuery
import com.secal.core.domain.catalog.usecase.GetCategoriesUseCase
import com.secal.core.domain.catalog.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Anasayfa durumu: kategori şeridi + öne çıkan ürünler. Kısmi içerik desteklenir. */
data class HomeUiState(
    val loading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val featured: List<Product> = emptyList(),
    val failed: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val getProducts: GetProductsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(loading = true, failed = false) }
        viewModelScope.launch {
            val categories = (getCategories() as? DataResult.Success)?.data.orEmpty()
            val productsResult = getProducts(ProductQuery())
            val featured = (productsResult as? DataResult.Success)?.data.orEmpty().take(FEATURED_LIMIT)
            _state.update {
                it.copy(
                    loading = false,
                    categories = categories,
                    featured = featured,
                    failed = productsResult is DataResult.Failure && categories.isEmpty(),
                )
            }
        }
    }

    private companion object {
        const val FEATURED_LIMIT = 10
    }
}
