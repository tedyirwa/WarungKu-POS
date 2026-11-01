package com.teds.warungku.pos.presentation.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.model.Transaction
import com.teds.warungku.pos.domain.model.TransactionItem
import com.teds.warungku.pos.domain.usecase.GetTransactionByIdUseCase
import com.teds.warungku.pos.domain.usecase.GetTransactionItemsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TransactionDetailViewModel(
    getTransactionByIdUseCase: GetTransactionByIdUseCase,
    getTransactionItemsUseCase: GetTransactionItemsUseCase,
    transactionId: Long
) : ViewModel() {

    val transaction: StateFlow<Transaction?> =
        getTransactionByIdUseCase(transactionId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val transactionItems: StateFlow<List<TransactionItem>> =
        getTransactionItemsUseCase(transactionId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}