package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.ProductRepository

class GetLowStockProductCountUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke() = repository.getLowStockProductCount()
}