package com.teds.warungku.pos.domain.repository

import com.teds.warungku.pos.data.local.entity.DailyRevenueResult
import com.teds.warungku.pos.domain.model.CartItem
import com.teds.warungku.pos.domain.model.DailyRevenue
import com.teds.warungku.pos.domain.model.Transaction
import com.teds.warungku.pos.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getAllTransactions(): Flow<List<Transaction>>

    fun getTodayRevenue(): Flow<Long>

    fun getTodayTransactionCount(): Flow<Int>

    suspend fun checkout(cartItems: List<CartItem>, payment: Long) : Long

    fun getTransactionItems(
        transactionId: Long
    ): Flow<List<TransactionItem>>

    fun getTransactionById(transactionId: Long): Flow<Transaction?>

    fun getRevenueLast7Days(): Flow<List<DailyRevenue>>
}