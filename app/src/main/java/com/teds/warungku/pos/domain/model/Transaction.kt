package com.teds.warungku.pos.domain.model

data class Transaction(
    val id: Long,
    val date: Long,
    val total: Long,
    val payment: Long,
    val change: Long
)
