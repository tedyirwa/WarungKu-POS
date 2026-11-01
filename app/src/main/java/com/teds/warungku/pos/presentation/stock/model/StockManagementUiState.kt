package com.teds.warungku.pos.presentation.stock.model

data class StockManagementUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)