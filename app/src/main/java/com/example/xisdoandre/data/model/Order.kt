package com.example.xisdoandre.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

// Representa um item individual dentro de um pedido
data class OrderItem(
    val id: String = "",
    val nome: String = "",
    val preco: Double = 0.0
)

// Representa o endereço de entrega para um pedido
data class DeliveryAddress(
    val bairro: String = "",
    val endereco: String = "",
    val numero: String = "",
    val complemento: String = ""
)

// Representa a estrutura completa de um pedido a ser salvo no Firestore
data class Order(
    val items: List<OrderItem> = emptyList(),
    val address: DeliveryAddress = DeliveryAddress(),
    val paymentMethod: String = "",
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val timestamp: FieldValue? = null, // Usado para escrita
    val status: String = "Pendente"
)

// Representa um pedido lido do histórico do Firestore
data class OrderHistoryItem(
    val id: String = "",
    val total: Double = 0.0,
    val status: String = "",
    val timestamp: Timestamp? = null, // Usado para leitura
    val items: List<OrderItem> = emptyList()
)
