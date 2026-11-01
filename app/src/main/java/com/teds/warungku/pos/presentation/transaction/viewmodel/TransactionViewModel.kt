package com.teds.warungku.pos.presentation.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.model.Transaction
import com.teds.warungku.pos.domain.usecase.GetAllTransactionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TransactionViewModel(
    getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModel() {

    val transactions: StateFlow<List<Transaction>> =
        getAllTransactionsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}