package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.StockMovementRepository

class StockInUseCase(
    private val repository: StockMovementRepository
) {
    suspend operator fun invoke(
        productId: Long,
        quantity: Int,
        note: String? = null
    ) {
        require(quantity > 0) {
            "Jumlah stok masuk harus lebih dari nol"
        }

        repository.changeStock(
            productId = productId,
            quantityChange = quantity,
            type = "IN",
            note = note
        )
    }
}