
package com.teds.warungku.pos.data.repository

import androidx.room.withTransaction
import com.teds.warungku.pos.core.common.getTodayRange
import com.teds.warungku.pos.data.local.database.ProductDatabase
import com.teds.warungku.pos.data.local.entity.StockMovementEntity
import com.teds.warungku.pos.data.local.entity.TransactionEntity
import com.teds.warungku.pos.data.local.entity.TransactionItemEntity
import com.teds.warungku.pos.domain.model.CartItem
import com.teds.warungku.pos.domain.model.Transaction
import com.teds.warungku.pos.domain.model.TransactionItem
import com.teds.warungku.pos.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val database: ProductDatabase
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return database.transactionDao()
            .getAllTransactions()
            .map { entities ->
                entities.map { entity ->
                    Transaction(
                        id = entity.id,
                        date = entity.date,
                        total = entity.total,
                        payment = entity.payment,
                        change = entity.change
                    )
                }
            }
    }

    override suspend fun checkout(
        cartItems: List<CartItem>,
        payment: Long
    ): Long {
        require(cartItems.isNotEmpty()) {
            "Keranjang belanja kosong"
        }

        val checkoutItems = cartItems
            .groupBy { it.productId }
            .map { (_, items) ->
                val firstItem = items.first()

                check(items.all {
                    it.price == firstItem.price &&
                            it.productName == firstItem.productName
                }) {
                    "Data produk ${firstItem.productName} tidak konsisten"
                }

                firstItem.copy(
                    quantity = items.sumOf { it.quantity }
                )
            }

        require(checkoutItems.all {
            it.quantity > 0 && it.price >= 0
        }) {
            "Data produk tidak valid"
        }

        val total = checkoutItems.sumOf { it.subtotal }

        require(total > 0) {
            "Total transaksi harus lebih dari nol"
        }

        require(payment >= total) {
            "Pembayaran tidak mencukupi"
        }

        val change = payment - total

        return database.withTransaction {

            val productDao = database.productDao()
            val transactionDao = database.transactionDao()
            val stockMovementDao = database.stockMovementDao()

            // 1. Validasi seluruh produk dan stok
            checkoutItems.forEach { item ->
                val product = productDao.getProductById(item.productId)

                check(product != null) {
                    "Produk ${item.productName} tidak ditemukan"
                }

                check(product.isActive) {
                    "Produk ${item.productName} tidak aktif"
                }

                check(product.stock >= item.quantity) {
                    "Stok ${item.productName} tidak mencukupi"
                }
            }

            // 2. Simpan transaksi utama
            val transactionId = transactionDao.insertTransaction(
                TransactionEntity(
                    date = System.currentTimeMillis(),
                    total = total,
                    payment = payment,
                    change = change
                )
            )

            // 3. Simpan detail transaksi
            val transactionItems = checkoutItems.map { item ->
                TransactionItemEntity(
                    transactionId = transactionId,
                    productId = item.productId,
                    productName = item.productName,
                    price = item.price,
                    quantity = item.quantity,
                    subtotal = item.subtotal
                )
            }

            transactionDao.insertTransactionItems(transactionItems)

            // 4. Kurangi stok dan catat Stock Movement SALE
            checkoutItems.forEach { item ->

                val product = productDao.getProductById(item.productId)
                    ?: error("Produk ${item.productName} tidak ditemukan")

                val stockBefore = product.stock
                val stockAfter = stockBefore - item.quantity

                val updatedRows = productDao.decreaseStock(
                    productId = item.productId,
                    quantity = item.quantity
                )

                check(updatedRows == 1) {
                    "Gagal memperbarui stok ${item.productName}"
                }

                stockMovementDao.insert(
                    StockMovementEntity(
                        productId = product.id,
                        productName = product.name,
                        type = "SALE",
                        quantityChange = -item.quantity,
                        stockBefore = stockBefore,
                        stockAfter = stockAfter,
                        note = "Transaksi #$transactionId",
                        createdAt = System.currentTimeMillis()
                    )
                )
            }

            transactionId
        }
    }

    override fun getTransactionItems(
        transactionId: Long
    ): Flow<List<TransactionItem>> {
        return database.transactionDao()
            .getTransactionItems(transactionId)
            .map { entities ->
                entities.map { entity ->
                    TransactionItem(
                        id = entity.id,
                        transactionId = entity.transactionId,
                        productId = entity.productId,
                        productName = entity.productName,
                        price = entity.price,
                        quantity = entity.quantity,
                        subtotal = entity.subtotal
                    )
                }
            }
    }

    override fun getTransactionById(
        transactionId: Long
    ): Flow<Transaction?> {
        return database.transactionDao()
            .getTransactionById(transactionId)
            .map { entity ->
                entity?.let {
                    Transaction(
                        id = it.id,
                        date = it.date,
                        total = it.total,
                        payment = it.payment,
                        change = it.change
                    )
                }
            }
    }

    override fun getTodayRevenue(): Flow<Long> {
        val range = getTodayRange()

        return database.transactionDao().getTodayRevenue(
            startOfDay = range.startOfDay,
            startOfNextDay = range.startOfNextDay
        )
    }

    override fun getTodayTransactionCount(): Flow<Int> {
        val range = getTodayRange()

        return database.transactionDao().getTodayTransactionCount(
            startOfDay = range.startOfDay,
            startOfNextDay = range.startOfNextDay
        )
    }
}
