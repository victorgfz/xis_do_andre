package com.example.xisdoandre.data.repository

import com.example.xisdoandre.data.model.Order
import com.example.xisdoandre.data.model.OrderHistoryItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun placeOrder(order: Order): Result<String>
    fun getOrderHistory(): Flow<List<OrderHistoryItem>>
}
