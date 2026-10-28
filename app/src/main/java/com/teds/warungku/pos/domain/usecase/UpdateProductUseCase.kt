package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.repository.ProductRepository

class UpdateProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.updateProduct(product)
    }
}