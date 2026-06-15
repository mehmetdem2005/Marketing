package com.secal.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secal.core.common.result.DataResult
import com.secal.core.domain.cart.CartItem
import com.secal.core.domain.cart.CartRepository
import com.secal.core.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cart: CartRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<CartItem>>>(UiState.Loading)
    val state: StateFlow<UiState<List<CartItem>>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = cart.getCart()) {
                is DataResult.Success ->
                    if (result.data.isEmpty()) UiState.Empty else UiState.Content(result.data)
                is DataResult.Failure -> UiState.Error(result.error)
            }
        }
    }

    /** Adet değiştir; 0'a inerse satırı kaldır. Sonuç başarılıysa listeyi tazele. */
    fun changeQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            val result = if (quantity <= 0) {
                cart.removeItem(itemId)
            } else {
                cart.updateQuantity(itemId, quantity)
            }
            if (result is DataResult.Success) load()
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            if (cart.removeItem(itemId) is DataResult.Success) load()
        }
    }
}
