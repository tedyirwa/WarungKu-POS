package com.teds.warungku.pos.presentation.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductViewModel(
    getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    val products: StateFlow<List<Product>> =
        getAllProductsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}