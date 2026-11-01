
package com.teds.warungku.pos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_movements")
data class StockMovementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val productName: String,
    val type: String,
    val quantityChange: Int,
    val stockBefore: Int,
    val stockAfter: Int,
    val note: String? = null,
    val createdAt: Long
)
