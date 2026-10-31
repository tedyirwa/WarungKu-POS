package com.teds.warungku.pos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teds.warungku.pos.data.local.entity.TransactionEntity
import com.teds.warungku.pos.data.local.entity.TransactionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(
        transaction: TransactionEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransactionItems(
        items: List<TransactionItemEntity>
    )

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
}