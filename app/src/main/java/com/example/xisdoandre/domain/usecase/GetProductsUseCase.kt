package com.example.xisdoandre.domain.usecase

import com.example.xisdoandre.data.model.Product
import com.example.xisdoandre.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}
