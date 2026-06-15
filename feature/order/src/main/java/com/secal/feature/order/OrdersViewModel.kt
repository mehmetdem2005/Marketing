package com.secal.feature.order

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

/** Siparişlerim listesi (4-durum). */
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orders: OrderRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Order>>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = orders.getMyOrders()) {
                is DataResult.Success ->
                    if (result.data.isEmpty()) UiState.Empty else UiState.Content(result.data)
                is DataResult.Failure -> UiState.Error(result.error)
            }
        }
    }
}
