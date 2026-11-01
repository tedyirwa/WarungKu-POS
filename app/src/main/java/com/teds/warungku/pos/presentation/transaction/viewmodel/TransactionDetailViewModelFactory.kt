package com.teds.warungku.pos.presentation.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teds.warungku.pos.domain.usecase.GetTransactionByIdUseCase
import com.teds.warungku.pos.domain.usecase.GetTransactionItemsUseCase

class TransactionDetailViewModelFactory(
    private val getTransactionItemsUseCase: GetTransactionItemsUseCase,
    private val transactionId: Long,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionDetailViewModel(
                getTransactionByIdUseCase,
                getTransactionItemsUseCase,
                transactionId
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}