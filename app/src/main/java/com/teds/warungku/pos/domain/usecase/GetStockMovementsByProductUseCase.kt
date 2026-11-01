package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.StockMovementRepository

class GetStockMovementsByProductUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke(productId: Long) =
        repository.getMovementsByProduct(productId)
}