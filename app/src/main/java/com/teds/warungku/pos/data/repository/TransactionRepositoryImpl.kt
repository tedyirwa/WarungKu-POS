package com.teds.warungku.pos.data.repository

import com.teds.warungku.pos.data.local.database.ProductDatabase
import com.teds.warungku.pos.data.local.entity.TransactionEntity
import com.teds.warungku.pos.data.local.entity.TransactionItemEntity
import com.teds.warungku.pos.domain.model.CartItem
import com.teds.warungku.pos.domain.model.Transaction
import com.teds.warungku.pos.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.room.withTransaction

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

        require(cartItems.all { it.quantity > 0 && it.price >= 0 }) {
            "Data produk tidak valid"
        }

        val total = cartItems.sumOf { it.subtotal }

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

            // 1. Validasi seluruh produk dan stok
            cartItems.forEach { item ->
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
            val transactionItems = cartItems.map { item ->
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

            // 4. Kurangi stok
            cartItems.forEach { item ->
                val updatedRows = productDao.decreaseStock(
                    productId = item.productId,
                    quantity = item.quantity
                )

                check(updatedRows == 1) {
                    "Gagal memperbarui stok ${item.productName}"
                }
            }

            transactionId
        }
    }
}