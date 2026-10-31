package com.teds.warungku.pos.presentation.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teds.warungku.pos.domain.usecase.ActivateProductUseCase
import com.teds.warungku.pos.domain.usecase.AddProductUseCase
import com.teds.warungku.pos.domain.usecase.DeactivateProductUseCase
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import com.teds.warungku.pos.domain.usecase.UpdateProductUseCase

class ProductViewModelFactory(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deactivateProductUseCase: DeactivateProductUseCase,
    private val activateProductUseCase: ActivateProductUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(
                getAllProductsUseCase,
                addProductUseCase,
                updateProductUseCase,
                deactivateProductUseCase,
                activateProductUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}