package com.teds.warungku.pos.presentation.cashier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teds.warungku.pos.domain.model.CartItem
import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.usecase.CheckoutUseCase
import com.teds.warungku.pos.domain.usecase.GetActiveProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CashierViewModel(
    getActiveProductsUseCase: GetActiveProductsUseCase,
    private val checkoutUseCase: CheckoutUseCase
) : ViewModel() {

    private val _isCheckingOut = MutableStateFlow(false)
    val isCheckingOut = _isCheckingOut.asStateFlow()

    private val _checkoutError = MutableStateFlow<String?>(null)
    val checkoutError = _checkoutError.asStateFlow()

    private val _lastTransactionId = MutableStateFlow<Long?>(null)
    val lastTransactionId = _lastTransactionId.asStateFlow()

    val products: StateFlow<List<Product>> =
        getActiveProductsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    private val _cartError = MutableStateFlow<String?>(null)
    val cartError = _cartError.asStateFlow()

    fun addToCart(product: Product) {
        val currentCart = _cartItems.value

        val existingItem = currentCart.find {
            it.productId == product.id
        }

        val currentQuantity = existingItem?.quantity ?: 0

        if (currentQuantity >= product.stock) {
            _cartError.value = "Stok ${product.name} tidak mencukupi"
            return
        }

        _cartError.value = null

        _cartItems.value = if (existingItem != null) {
            currentCart.map { item ->
                if (item.productId == product.id) {
                    item.copy(quantity = item.quantity + 1)
                } else {
                    item
                }
            }
        } else {
            currentCart + CartItem(
                productId = product.id,
                productName = product.name,
                price = product.price,
                quantity = 1
            )
        }
    }

    fun removeFromCart(productId: Long) {
        _cartError.value = null

        val currentCart = _cartItems.value

        _cartItems.value = currentCart.mapNotNull { item ->
            if (item.productId == productId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1)
                } else {
                    null
                }
            } else {
                item
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _cartError.value = null
    }

    val total = cartItems.map { items ->
        items.sumOf { it.subtotal }
    }

    private val _payment = MutableStateFlow(0L)
    val payment = _payment.asStateFlow()

    fun updatePayment(amount: Long) {
        _payment.value = amount.coerceAtLeast(0L)
    }

    val change = combine(total, payment) { total, payment ->
        (payment - total).coerceAtLeast(0L)
    }

    fun checkout() {
        if (_isCheckingOut.value) return

        viewModelScope.launch {
            _isCheckingOut.value = true
            _checkoutError.value = null

            try {
                val transactionId = checkoutUseCase(
                    cartItems = _cartItems.value,
                    payment = _payment.value
                )

                _lastTransactionId.value = transactionId
                clearCart()
                updatePayment(0L)

            } catch (e: Exception) {
                _checkoutError.value = e.message ?: "Checkout gagal"
            } finally {
                _isCheckingOut.value = false
            }
        }
    }
}

