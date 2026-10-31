package com.teds.warungku.pos.domain.model

data class CartItem(
    val productId: Long,
    val productName: String,
    val price: Long,
    val quantity: Int
) {
    val subtotal: Long
        get() = price * quantity
}