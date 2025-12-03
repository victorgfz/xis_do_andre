package com.example.xisdoandre.domain.usecase

import com.example.xisdoandre.data.model.Order
import com.example.xisdoandre.domain.repository.OrderRepository

class PlaceOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(order: Order): Result<String> {
        return orderRepository.placeOrder(order)
    }
}
