package com.teds.warungku.pos.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.usecase.GetTodayRevenueUseCase
import com.teds.warungku.pos.domain.usecase.GetTodayTransactionCountUseCase
import com.teds.warungku.pos.domain.usecase.GetActiveProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetLowStockProductCountUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val getTodayRevenueUseCase: GetTodayRevenueUseCase,
    private val getTodayTransactionCountUseCase: GetTodayTransactionCountUseCase,
    private val getActiveProductsUseCase: GetActiveProductsUseCase,
    private val getLowStockProductCountUseCase: GetLowStockProductCountUseCase
) : ViewModel() {

    val uiState = combine(
        getTodayRevenueUseCase(),
        getTodayTransactionCountUseCase(),
        getActiveProductsUseCase(),
        getLowStockProductCountUseCase()
    ) { revenue, transactionCount, products, lowStockCount ->

        DashboardUiState(
            todayRevenue = revenue,
            todayTransactionCount = transactionCount,
            activeProductCount = products.size,
            lowStockProductCount = lowStockCount,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

}