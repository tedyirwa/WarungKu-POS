package com.teds.warungku.pos.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.usecase.GetTodayRevenueUseCase
import com.teds.warungku.pos.domain.usecase.GetTodayTransactionCountUseCase
import com.teds.warungku.pos.domain.usecase.GetActiveProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetLowStockProductCountUseCase
import com.teds.warungku.pos.domain.usecase.GetOutOfStockProductCountUseCase
import com.teds.warungku.pos.domain.usecase.GetRevenueLast7DaysUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val getTodayRevenueUseCase: GetTodayRevenueUseCase,
    private val getTodayTransactionCountUseCase: GetTodayTransactionCountUseCase,
    private val getActiveProductsUseCase: GetActiveProductsUseCase,
    private val getLowStockProductCountUseCase: GetLowStockProductCountUseCase,
    private val getOutOfStockProductCountUseCase: GetOutOfStockProductCountUseCase,
    private val getRevenueLast7DaysUseCase: GetRevenueLast7DaysUseCase
) : ViewModel() {

    private val summaryFlow = combine(
        getTodayRevenueUseCase(),
        getTodayTransactionCountUseCase(),
        getActiveProductsUseCase()
    ) { revenue, transactionCount, products ->
        Triple(revenue, transactionCount, products)
    }

    private val stockFlow = combine(
        getLowStockProductCountUseCase(),
        getOutOfStockProductCountUseCase(),
        getRevenueLast7DaysUseCase()
    ) { lowStockCount, outOfStockCount, last7DaysRevenue ->
        Triple(lowStockCount, outOfStockCount, last7DaysRevenue)
    }

    val uiState = combine(summaryFlow, stockFlow) { summary, stock ->
        DashboardUiState(
            todayRevenue = summary.first,
            todayTransactionCount = summary.second,
            activeProductCount = summary.third.size,
            lowStockProductCount = stock.first,
            outOfStockProductCount = stock.second,
            last7DaysRevenue = stock.third,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

}