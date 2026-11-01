
package com.teds.warungku.pos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teds.warungku.pos.data.local.entity.StockMovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementDao {

    @Insert
    suspend fun insert(movement: StockMovementEntity)

    @Query("""
        SELECT * FROM stock_movements
        ORDER BY createdAt DESC
    """)
    fun getAllMovements(): Flow<List<StockMovementEntity>>

    @Query("""
        SELECT * FROM stock_movements
        WHERE productId = :productId
        ORDER BY createdAt DESC
    """)
    fun getMovementsByProduct(productId: Long): Flow<List<StockMovementEntity>>
}
