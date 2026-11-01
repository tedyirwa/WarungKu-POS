
package com.teds.warungku.pos.domain.repository

import com.teds.warungku.pos.domain.model.StockMovement
import kotlinx.coroutines.flow.Flow

interface StockMovementRepository {

    fun getAllMovements(): Flow<List<StockMovement>>

    fun getMovementsByProduct(
        productId: Long
    ): Flow<List<StockMovement>>

    suspend fun changeStock(
        productId: Long,
        quantityChange: Int,
        type: String,
        note: String? = null
    )
}

