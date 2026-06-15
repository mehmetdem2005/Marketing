package com.koyden.feature.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.catalog.Category
import com.koyden.core.domain.catalog.Product
import com.koyden.core.domain.catalog.ProductQuery
import com.koyden.core.domain.catalog.usecase.GetCategoriesUseCase
import com.koyden.core.domain.catalog.usecase.GetProductsUseCase
import com.koyden.core.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Katalog/keşif durumu (tek-yönlü). Kategoriler filtre çubuğunu besler; [products] 4-durum.
 * Kategori seçimi/arama → ürünler yeniden yüklenir (sunucu-taraflı filtre, RLS public read).
 */
data class CatalogUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val search: String = "",
    val products: UiState<List<Product>> = UiState.Loading,
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val getProducts: GetProductsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state: StateFlow<CatalogUiState> = _state.asStateFlow()

    init {
        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            (getCategories() as? DataResult.Success)?.let { result ->
                _state.update { it.copy(categories = result.data) }
            }
        }
    }

    fun loadProducts() {
        _state.update { it.copy(products = UiState.Loading) }
        viewModelScope.launch {
            val current = _state.value
            val query = ProductQuery(
                categoryId = current.selectedCategoryId,
                search = current.search.ifBlank { null },
            )
            _state.update {
                it.copy(
                    products = when (val result = getProducts(query)) {
                        is DataResult.Success ->
                            if (result.data.isEmpty()) UiState.Empty else UiState.Content(result.data)
                        is DataResult.Failure -> UiState.Error(result.error)
                    },
                )
            }
        }
    }

    /** Aynı kategoriye tekrar dokunmak filtreyi kaldırır (toggle). */
    fun onCategorySelect(categoryId: String?) {
        _state.update {
            it.copy(selectedCategoryId = if (it.selectedCategoryId == categoryId) null else categoryId)
        }
        loadProducts()
    }

    fun onSearchChange(value: String) = _state.update { it.copy(search = value) }

    fun onSearchSubmit() = loadProducts()
}
