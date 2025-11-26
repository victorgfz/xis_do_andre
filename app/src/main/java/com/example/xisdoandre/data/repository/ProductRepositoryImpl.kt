package com.example.xisdoandre.data.repository

import com.example.xisdoandre.data.model.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ProductRepositoryImpl : ProductRepository {
    override fun getAllProducts(): Flow<List<Product>> = callbackFlow {
        val db = Firebase.firestore
        val listener = db.collection("produtos")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val productList = snapshot.toObjects(Product::class.java)
                    trySend(productList).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }
}
