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

    @Query(
        """
    SELECT * FROM transaction_items
    WHERE transactionId = :transactionId
    ORDER BY id ASC
    """
    )
    fun getTransactionItems(
        transactionId: Long
    ): Flow<List<TransactionItemEntity>>

    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    fun getTransactionById(
        transactionId: Long
    ): Flow<TransactionEntity?>

    @Query(
        """
    SELECT COALESCE(SUM(total), 0)
    FROM transactions
    WHERE date >= :startOfDay AND date < :startOfNextDay
    """
    )
    fun getTodayRevenue(
        startOfDay: Long,
        startOfNextDay: Long
    ): Flow<Long>

    @Query(
        """
    SELECT COUNT(*)
    FROM transactions
    WHERE date >= :startOfDay AND date < :startOfNextDay
    """
    )
    fun getTodayTransactionCount(
        startOfDay: Long,
        startOfNextDay: Long
    ): Flow<Int>
}