package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.ProductRepository

class DeactivateProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Long) {
        repository.deactivateProduct(productId)
    }
}