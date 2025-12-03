package com.example.xisdoandre.domain.usecase

import com.example.xisdoandre.data.model.OrderHistoryItem
import com.example.xisdoandre.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrderHistoryUseCase(private val orderRepository: OrderRepository) {
    operator fun invoke(): Flow<List<OrderHistoryItem>> {
        return orderRepository.getOrderHistory()
    }
}
