
package com.teds.warungku.pos.presentation.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teds.warungku.pos.domain.usecase.AdjustStockUseCase
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetAllStockMovementsUseCase
import com.teds.warungku.pos.domain.usecase.GetStockMovementsByProductUseCase
import com.teds.warungku.pos.domain.usecase.StockInUseCase

class StockManagementViewModelFactory(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getAllStockMovementsUseCase: GetAllStockMovementsUseCase,
    private val stockInUseCase: StockInUseCase,
    private val adjustStockUseCase: AdjustStockUseCase,
    private val getStockMovementsByProductUseCase: GetStockMovementsByProductUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(StockManagementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockManagementViewModel(
                getAllProductsUseCase,
                getAllStockMovementsUseCase,
                stockInUseCase,
                adjustStockUseCase,
                getStockMovementsByProductUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
