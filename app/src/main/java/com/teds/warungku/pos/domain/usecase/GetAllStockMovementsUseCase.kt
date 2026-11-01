package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.StockMovementRepository

class GetAllStockMovementsUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke() = repository.getAllMovements()
}