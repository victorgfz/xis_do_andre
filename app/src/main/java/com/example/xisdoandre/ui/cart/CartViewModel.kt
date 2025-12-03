package com.example.xisdoandre.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xisdoandre.data.model.DeliveryAddress
import com.example.xisdoandre.data.model.Order
import com.example.xisdoandre.data.model.OrderItem
import com.example.xisdoandre.data.model.Product
import com.example.xisdoandre.data.repository.OrderRepositoryImpl
import com.example.xisdoandre.domain.usecase.PlaceOrderUseCase
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado da UI para a finalização do pedido
sealed class PlaceOrderUiState {
    object Idle : PlaceOrderUiState()
    object Loading : PlaceOrderUiState()
    data class Success(val orderId: String) : PlaceOrderUiState()
    data class Error(val message: String) : PlaceOrderUiState()
}

class CartViewModel : ViewModel() {

    private val placeOrderUseCase = PlaceOrderUseCase(OrderRepositoryImpl())

    private val _placeOrderState = MutableStateFlow<PlaceOrderUiState>(PlaceOrderUiState.Idle)
    val placeOrderState: StateFlow<PlaceOrderUiState> = _placeOrderState.asStateFlow()

    fun placeOrder(
        cartItems: List<Product>,
        address: DeliveryAddress,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            if (cartItems.isEmpty()) {
                _placeOrderState.value = PlaceOrderUiState.Error("O carrinho está vazio.")
                return@launch
            }

            _placeOrderState.value = PlaceOrderUiState.Loading

            val subtotal = cartItems.sumOf { it.preco }
            val deliveryFee = 5.0
            val total = subtotal + deliveryFee

            val order = Order(
                items = cartItems.map { OrderItem(it.id, it.nome, it.preco) },
                address = address,
                paymentMethod = paymentMethod,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                total = total,
                timestamp = FieldValue.serverTimestamp(),
                status = "Pendente"
            )

            val result = placeOrderUseCase(order)
            result.onSuccess {
                _placeOrderState.value = PlaceOrderUiState.Success(it)
            }.onFailure {
                _placeOrderState.value = PlaceOrderUiState.Error(it.message ?: "Erro desconhecido")
            }
        }
    }

    fun resetState() {
        _placeOrderState.value = PlaceOrderUiState.Idle
    }
}