package com.example.xisdoandre.data.repository

import com.example.xisdoandre.data.model.Order
import com.example.xisdoandre.data.model.OrderHistoryItem
import com.example.xisdoandre.domain.repository.OrderRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl : OrderRepository {
    override suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val db = Firebase.firestore
            val documentReference = db.collection("pedidos").add(order).await()
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOrderHistory(): Flow<List<OrderHistoryItem>> = callbackFlow {
        val db = Firebase.firestore
        val listener = db.collection("pedidos")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val orderList = snapshot.documents.mapNotNull {
                        it.toObject(OrderHistoryItem::class.java)?.copy(id = it.id)
                    }
                    trySend(orderList).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }
}