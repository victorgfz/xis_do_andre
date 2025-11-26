package com.example.xisdoandre.data.model

import com.google.firebase.firestore.PropertyName

// ============ DATA CLASS PARA PRODUTOS (COMPATÍVEL COM FIREBASE) ============
data class Product(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("nome") @set:PropertyName("nome") var nome: String = "",
    @get:PropertyName("descricao") @set:PropertyName("descricao") var descricao: String = "",
    @get:PropertyName("preco") @set:PropertyName("preco") var preco: Double = 0.0,
    @get:PropertyName("emoji") @set:PropertyName("emoji") var emoji: String = ""
)
