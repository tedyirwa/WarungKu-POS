package com.teds.warungku.pos.presentation.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teds.warungku.pos.domain.usecase.GetAllTransactionsUseCase

class TransactionViewModelFactory(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(
                getAllTransactionsUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}