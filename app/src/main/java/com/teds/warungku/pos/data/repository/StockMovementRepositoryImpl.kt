
package com.teds.warungku.pos.data.repository

import androidx.room.withTransaction
import com.teds.warungku.pos.data.local.database.ProductDatabase
import com.teds.warungku.pos.data.local.entity.StockMovementEntity
import com.teds.warungku.pos.domain.model.StockMovement
import com.teds.warungku.pos.domain.repository.StockMovementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockMovementRepositoryImpl(
    private val database: ProductDatabase
) : StockMovementRepository {

    override fun getAllMovements(): Flow<List<StockMovement>> {
        return database.stockMovementDao()
            .getAllMovements()
            .map { entities ->
                entities.map { entity ->
                    StockMovement(
                        id = entity.id,
                        productId = entity.productId,
                        productName = entity.productName,
                        type = entity.type,
                        quantityChange = entity.quantityChange,
                        stockBefore = entity.stockBefore,
                        stockAfter = entity.stockAfter,
                        note = entity.note,
                        createdAt = entity.createdAt
                    )
                }
            }
    }

    override fun getMovementsByProduct(
        productId: Long
    ): Flow<List<StockMovement>> {
        return database.stockMovementDao()
            .getMovementsByProduct(productId)
            .map { entities ->
                entities.map { entity ->
                    StockMovement(
                        id = entity.id,
                        productId = entity.productId,
                        productName = entity.productName,
                        type = entity.type,
                        quantityChange = entity.quantityChange,
                        stockBefore = entity.stockBefore,
                        stockAfter = entity.stockAfter,
                        note = entity.note,
                        createdAt = entity.createdAt
                    )
                }
            }
    }

    override suspend fun changeStock(
        productId: Long,
        quantityChange: Int,
        type: String,
        note: String?
    ) {
        require(quantityChange != 0) {
            "Perubahan stok tidak boleh nol"
        }

        database.withTransaction {
            val productDao = database.productDao()
            val movementDao = database.stockMovementDao()

            val product = productDao.getProductById(productId)
                ?: error("Produk tidak ditemukan")

            val stockBefore = product.stock

            val stockAfter = stockBefore + quantityChange

            require(stockAfter >= 0) {
                "Stok tidak boleh negatif"
            }

            val updatedRows = productDao.adjustStock(
                productId = productId,
                quantityChange = quantityChange
            )

            check(updatedRows == 1) {
                "Gagal memperbarui stok"
            }

            movementDao.insert(
                StockMovementEntity(
                    productId = product.id,
                    productName = product.name,
                    type = type,
                    quantityChange = quantityChange,
                    stockBefore = stockBefore,
                    stockAfter = stockAfter,
                    note = note,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}
