package com.teds.warungku.pos.presentation.dashboard.viewmodel

import com.teds.warungku.pos.domain.model.DailyRevenue

data class DashboardUiState(
    val todayRevenue: Long = 0L,
    val todayTransactionCount: Int = 0,
    val activeProductCount: Int = 0,
    val lowStockProductCount: Int = 0,
    val outOfStockProductCount: Int = 0,
    val isLoading: Boolean = true,
    val last7DaysRevenue: List<DailyRevenue> = emptyList(),
)