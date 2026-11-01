package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.TransactionRepository

class GetTransactionItemsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(transactionId: Long) =
        transactionRepository.getTransactionItems(transactionId)
}