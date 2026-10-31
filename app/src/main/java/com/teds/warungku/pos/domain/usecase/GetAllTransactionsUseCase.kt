package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.TransactionRepository

class GetAllTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke() =
        transactionRepository.getAllTransactions()
}