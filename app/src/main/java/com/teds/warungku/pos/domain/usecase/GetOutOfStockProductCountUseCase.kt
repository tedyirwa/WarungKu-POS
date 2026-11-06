package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetOutOfStockProductCountUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getOutOfStockProductCount()
    }
}