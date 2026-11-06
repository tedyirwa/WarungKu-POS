package com.teds.warungku.pos.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teds.warungku.pos.domain.usecase.GetTodayRevenueUseCase
import com.teds.warungku.pos.domain.usecase.GetTodayTransactionCountUseCase
import com.teds.warungku.pos.domain.usecase.GetActiveProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetLowStockProductCountUseCase
import com.teds.warungku.pos.domain.usecase.GetOutOfStockProductCountUseCase

class DashboardViewModelFactory(
    private val getTodayRevenueUseCase: GetTodayRevenueUseCase,
    private val getTodayTransactionCountUseCase: GetTodayTransactionCountUseCase,
    private val getActiveProductsUseCase: GetActiveProductsUseCase,
    private val getLowStockProductCountUseCase: GetLowStockProductCountUseCase,
    private val getOutOfStockProductCountUseCase: GetOutOfStockProductCountUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(
                getTodayRevenueUseCase,
                getTodayTransactionCountUseCase,
                getActiveProductsUseCase,
                getLowStockProductCountUseCase,
                getOutOfStockProductCountUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}