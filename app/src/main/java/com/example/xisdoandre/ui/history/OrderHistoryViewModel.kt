package com.example.xisdoandre.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xisdoandre.data.model.OrderHistoryItem
import com.example.xisdoandre.data.repository.OrderRepositoryImpl
import com.example.xisdoandre.domain.usecase.GetOrderHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

// Estado da UI para o histórico de pedidos
sealed class OrderHistoryUiState {
    object Loading : OrderHistoryUiState()
    data class Success(val orders: List<OrderHistoryItem>) : OrderHistoryUiState()
    data class Error(val message: String) : OrderHistoryUiState()
}

class OrderHistoryViewModel : ViewModel() {

    private val getOrderHistoryUseCase = GetOrderHistoryUseCase(OrderRepositoryImpl())

    private val _uiState = MutableStateFlow<OrderHistoryUiState>(OrderHistoryUiState.Loading)
    val uiState: StateFlow<OrderHistoryUiState> = _uiState

    init {
        fetchOrderHistory()
    }

    fun fetchOrderHistory() {
        viewModelScope.launch {
            getOrderHistoryUseCase()
                .onStart { _uiState.value = OrderHistoryUiState.Loading }
                .catch { e -> _uiState.value = OrderHistoryUiState.Error(e.message ?: "Erro desconhecido") }
                .collect { orders ->
                    _uiState.value = OrderHistoryUiState.Success(orders)
                }
        }
    }
}