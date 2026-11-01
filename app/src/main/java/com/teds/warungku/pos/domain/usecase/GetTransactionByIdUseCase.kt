package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.TransactionRepository

class GetTransactionByIdUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(transactionId: Long) =
        transactionRepository.getTransactionById(transactionId)
}