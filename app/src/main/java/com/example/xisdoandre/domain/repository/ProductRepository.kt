package com.example.xisdoandre.domain.repository

import com.example.xisdoandre.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
}
