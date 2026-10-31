package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.ProductRepository

class GetActiveProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke() =
        productRepository.getActiveProducts()
}