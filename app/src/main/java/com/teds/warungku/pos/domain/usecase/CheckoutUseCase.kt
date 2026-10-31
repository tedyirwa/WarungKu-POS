package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.model.CartItem
import com.teds.warungku.pos.domain.repository.TransactionRepository

class CheckoutUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        cartItems: List<CartItem>,
        payment: Long
    ): Long {
        return transactionRepository.checkout(
            cartItems = cartItems,
            payment = payment
        )
    }
}