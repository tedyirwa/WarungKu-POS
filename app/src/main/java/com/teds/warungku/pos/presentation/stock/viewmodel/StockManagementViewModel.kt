
package com.teds.warungku.pos.presentation.stock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.model.StockMovement
import com.teds.warungku.pos.domain.usecase.AdjustStockUseCase
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetAllStockMovementsUseCase
import com.teds.warungku.pos.domain.usecase.GetStockMovementsByProductUseCase
import com.teds.warungku.pos.domain.usecase.StockInUseCase
import com.teds.warungku.pos.presentation.stock.model.StockManagementUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StockManagementViewModel(
    getAllProductsUseCase: GetAllProductsUseCase,
    getAllStockMovementsUseCase: GetAllStockMovementsUseCase,
    private val stockInUseCase: StockInUseCase,
    private val adjustStockUseCase: AdjustStockUseCase,
    private val getStockMovementsByProductUseCase: GetStockMovementsByProductUseCase
) : ViewModel() {

    val products: StateFlow<List<Product>> =
        getAllProductsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val stockMovements: StateFlow<List<StockMovement>> =
        getAllStockMovementsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(StockManagementUiState())
    val uiState = _uiState.asStateFlow()

    fun stockIn(
        productId: Long,
        quantity: Int,
        note: String?
    ) {
        executeStockOperation {
            stockInUseCase(productId, quantity, note)
        }
    }

    fun adjustStock(
        productId: Long,
        quantityChange: Int,
        note: String?
    ) {
        executeStockOperation {
            adjustStockUseCase(productId, quantityChange, note)
        }
    }

    private fun executeStockOperation(
        operation: suspend () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = StockManagementUiState(isLoading = true)

            try {
                operation()

                _uiState.value = StockManagementUiState(
                    successMessage = "Perubahan stok berhasil disimpan"
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = StockManagementUiState(
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    fun getMovementsByProduct(productId: Long) =
        getStockMovementsByProductUseCase(productId)
}
