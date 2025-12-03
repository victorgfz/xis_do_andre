package com.example.xisdoandre.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class OrderItem(
    val id: String = "",
    val nome: String = "",
    val preco: Double = 0.0
)

data class DeliveryAddress(
    val bairro: String = "",
    val endereco: String = "",
    val numero: String = "",
    val complemento: String = ""
)

data class Order(
    val items: List<OrderItem> = emptyList(),
    val address: DeliveryAddress = DeliveryAddress(),
    val paymentMethod: String = "",
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val timestamp: FieldValue? = null, // Para escrita no servidor do Firestore
    val status: String = "Pendente"
)

data class OrderHistoryItem(
    val id: String = "",
    val total: Double = 0.0,
    val status: String = "",
    val timestamp: Timestamp? = null,
    val items: List<OrderItem> = emptyList()
)
