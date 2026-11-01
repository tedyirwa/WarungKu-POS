
package com.teds.warungku.pos.domain.model

data class StockMovement(
    val id: Long,
    val productId: Long,
    val productName: String,
    val type: String,
    val quantityChange: Int,
    val stockBefore: Int,
    val stockAfter: Int,
    val note: String?,
    val createdAt: Long
)
