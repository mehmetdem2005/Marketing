package com.secal.feature.catalog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secal.core.common.result.DataResult
import com.secal.core.domain.cart.CartRepository
import com.secal.core.domain.catalog.Product
import com.secal.core.domain.catalog.usecase.GetProductUseCase
import com.secal.core.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Sepete ekleme geri bildirimi (4-durum mikro-etkileşim). */
enum class CartStatus { Idle, Adding, Added, Error }

/** Ürün detay durumu (4-durum). [productId] navigasyon argümanından (SavedStateHandle) okunur. */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProduct: GetProductUseCase,
    private val cart: CartRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle[ARG_PRODUCT_ID]) {
        "productId navigasyon argümanı eksik"
    }

    private val _state = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val state: StateFlow<UiState<Product>> = _state.asStateFlow()

    private val _cartStatus = MutableStateFlow(CartStatus.Idle)
    val cartStatus: StateFlow<CartStatus> = _cartStatus.asStateFlow()

    init {
        load()
    }

    fun addToCart() {
        if (_cartStatus.value == CartStatus.Adding) return
        _cartStatus.value = CartStatus.Adding
        viewModelScope.launch {
            _cartStatus.value = when (cart.addToCart(productId, 1)) {
                is DataResult.Success -> CartStatus.Added
                is DataResult.Failure -> CartStatus.Error
            }
        }
    }

    fun consumeCartStatus() {
        _cartStatus.value = CartStatus.Idle
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = getProduct(productId)) {
                is DataResult.Success -> UiState.Content(result.data)
                is DataResult.Failure -> UiState.Error(result.error)
            }
        }
    }

    companion object {
        const val ARG_PRODUCT_ID = "productId"
    }
}
