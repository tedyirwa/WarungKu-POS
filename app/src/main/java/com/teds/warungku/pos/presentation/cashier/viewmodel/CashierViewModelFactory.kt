package com.teds.warungku.pos.presentation.cashier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teds.warungku.pos.domain.usecase.CheckoutUseCase
import com.teds.warungku.pos.domain.usecase.GetActiveProductsUseCase

class CashierViewModelFactory(
    private val getActiveProductsUseCase: GetActiveProductsUseCase,
    private val checkoutUseCase: CheckoutUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CashierViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CashierViewModel(
                getActiveProductsUseCase,
                checkoutUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}