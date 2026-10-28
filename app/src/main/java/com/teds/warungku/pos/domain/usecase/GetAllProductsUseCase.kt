package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetAllProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getAllProducts()
    }
}