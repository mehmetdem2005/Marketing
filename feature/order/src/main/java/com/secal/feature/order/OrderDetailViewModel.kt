package com.secal.feature.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secal.core.common.result.DataResult
import com.secal.core.domain.order.Order
import com.secal.core.domain.order.OrderRepository
import com.secal.core.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Sipariş detayı (4-durum). [orderId] navigasyon argümanından okunur. */
@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orders: OrderRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle[ARG_ORDER_ID]) {
        "orderId navigasyon argümanı eksik"
    }

    private val _state = MutableStateFlow<UiState<Order>>(UiState.Loading)
    val state: StateFlow<UiState<Order>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = orders.getOrder(orderId)) {
                is DataResult.Success -> UiState.Content(result.data)
                is DataResult.Failure -> UiState.Error(result.error)
            }
        }
    }

    companion object {
        const val ARG_ORDER_ID = "orderId"
    }
}
