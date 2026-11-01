package com.teds.warungku.pos.domain.usecase

import com.teds.warungku.pos.domain.repository.StockMovementRepository

class AdjustStockUseCase(
    private val repository: StockMovementRepository
) {
    suspend operator fun invoke(
        productId: Long,
        quantityChange: Int,
        note: String? = null
    ) {
        require(quantityChange != 0) {
            "Perubahan stok tidak boleh nol"
        }

        repository.changeStock(
            productId = productId,
            quantityChange = quantityChange,
            type = "ADJUSTMENT",
            note = note
        )
    }
}