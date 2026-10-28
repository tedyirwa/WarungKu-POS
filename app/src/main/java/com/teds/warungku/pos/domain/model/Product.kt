package com.teds.warungku.pos.domain.model

data class Product(
    val id: Long,
    val name: String,
    val price: Long,
    val stock: Int,
    val unit: String
)