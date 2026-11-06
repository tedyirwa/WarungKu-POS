package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.TransactionRepository

class GetTodayRevenueUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getTodayRevenue()
}