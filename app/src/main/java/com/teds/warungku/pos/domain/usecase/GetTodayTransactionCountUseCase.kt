package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.TransactionRepository

class GetTodayTransactionCountUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getTodayTransactionCount()
}