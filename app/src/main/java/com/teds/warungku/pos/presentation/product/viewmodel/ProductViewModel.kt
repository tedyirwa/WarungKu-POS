package com.teds.warungku.pos.presentation.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.usecase.ActivateProductUseCase
import com.teds.warungku.pos.domain.usecase.AddProductUseCase
import com.teds.warungku.pos.domain.usecase.DeactivateProductUseCase
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import com.teds.warungku.pos.domain.usecase.UpdateProductUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deactivateProductUseCase: DeactivateProductUseCase,
    private val activateProductUseCase: ActivateProductUseCase
) : ViewModel() {

    val products: StateFlow<List<Product>> =
        getAllProductsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addProduct(product: Product) {
        viewModelScope.launch {
            addProductUseCase(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            updateProductUseCase(product)
        }
    }

    fun deactivateProduct(productId: Long) {
        viewModelScope.launch {
            deactivateProductUseCase(productId)
        }
    }

    fun activateProduct(productId: Long) {
        viewModelScope.launch {
            activateProductUseCase(productId)
        }
    }
}