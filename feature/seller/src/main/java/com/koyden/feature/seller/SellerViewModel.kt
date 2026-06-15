package com.koyden.feature.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.catalog.Product
import com.koyden.core.domain.catalog.Store
import com.koyden.core.domain.seller.SellerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SellerPhase { Loading, NeedsStore, Ready, Error }

/** Satıcı paneli durumu (tek-yönlü). Mağaza yoksa kurulum formu, varsa ürün listesi. */
data class SellerUiState(
    val phase: SellerPhase = SellerPhase.Loading,
    val store: Store? = null,
    val products: List<Product> = emptyList(),
    val error: AppError? = null,
    val saving: Boolean = false,
    val name: String = "",
    val city: String = "",
    val description: String = "",
)

@HiltViewModel
class SellerViewModel @Inject constructor(
    private val seller: SellerRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SellerUiState())
    val state: StateFlow<SellerUiState> = _state.asStateFlow()

    init {
        loadStore()
    }

    fun loadStore() {
        _state.update { it.copy(phase = SellerPhase.Loading, error = null) }
        viewModelScope.launch {
            when (val result = seller.getMyStore()) {
                is DataResult.Success -> {
                    val store = result.data
                    if (store == null) {
                        _state.update { it.copy(phase = SellerPhase.NeedsStore) }
                    } else {
                        _state.update { it.copy(phase = SellerPhase.Ready, store = store) }
                        loadProducts(store.id)
                    }
                }
                is DataResult.Failure ->
                    _state.update { it.copy(phase = SellerPhase.Error, error = result.error) }
            }
        }
    }

    fun refreshProducts() = _state.value.store?.let { loadProducts(it.id) }

    private fun loadProducts(storeId: String) {
        viewModelScope.launch {
            (seller.getMyProducts(storeId) as? DataResult.Success)?.let { result ->
                _state.update { it.copy(products = result.data) }
            }
        }
    }

    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onCityChange(value: String) = _state.update { it.copy(city = value) }
    fun onDescriptionChange(value: String) = _state.update { it.copy(description = value) }

    fun createStore() {
        val form = _state.value
        if (form.name.isBlank() || form.saving) return
        _state.update { it.copy(saving = true, error = null) }
        viewModelScope.launch {
            when (val result = seller.createStore(form.name, form.city, form.description)) {
                is DataResult.Success ->
                    _state.update {
                        it.copy(saving = false, phase = SellerPhase.Ready, store = result.data)
                    }
                is DataResult.Failure ->
                    _state.update { it.copy(saving = false, error = result.error) }
            }
        }
    }
}
