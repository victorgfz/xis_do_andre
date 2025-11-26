package com.example.xisdoandre.data.repository

import com.example.xisdoandre.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
}
