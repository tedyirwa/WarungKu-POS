package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.model.DailyRevenue
import com.teds.warungku.pos.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetRevenueLast7DaysUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<DailyRevenue>> {
        return repository.getRevenueLast7Days()
    }
}