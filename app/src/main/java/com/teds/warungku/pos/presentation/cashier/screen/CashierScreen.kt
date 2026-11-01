package com.teds.warungku.pos.presentation.cashier.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.teds.warungku.pos.presentation.cashier.viewmodel.CashierViewModel

@Composable
fun CashierScreen(
    viewModel: CashierViewModel,
    onBack: () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val cartError by viewModel.cartError.collectAsState()
    val total by viewModel.total.collectAsState(initial = 0L)
    val payment by viewModel.payment.collectAsState()
    val change by viewModel.change.collectAsState(initial = 0L)
    val isCheckingOut by viewModel.isCheckingOut.collectAsState()
    val checkoutError by viewModel.checkoutError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Kasir",
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(onClick = onBack) {
                Text("Kembali")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(product.name)
                            Text("Rp ${product.price}")
                            Text("Stok: ${product.stock}")
                        }

                        TextButton(
                            onClick = {
                                viewModel.addToCart(product)
                            }
                        ) {
                            Text("Tambah")
                        }
                    }
                }
            }
        }

        cartError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "Keranjang (${cartItems.size} produk)",
            style = MaterialTheme.typography.titleLarge
        )

        cartItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.productName)
                    Text("${item.quantity} x Rp ${item.price}")
                }

                Row {
                    TextButton(
                        onClick = {
                            viewModel.removeFromCart(item.productId)
                        }
                    ) {
                        Text("-")
                    }

                    TextButton(
                        onClick = {
                            val product = products.find {
                                it.id == item.productId
                            }
                            product?.let {
                                viewModel.addToCart(it)
                            }
                        }
                    ) {
                        Text("+")
                    }
                }
            }
        }

        Text(
            text = "Total: Rp $total",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = if (payment == 0L) "" else payment.toString(),
            onValueChange = { value ->
                viewModel.updatePayment(value.toLongOrNull() ?: 0L)
            },
            label = { Text("Uang pembayaran") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Kembalian: Rp $change")

        checkoutError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                viewModel.checkout()
            },
            enabled = cartItems.isNotEmpty() &&
                    payment >= total &&
                    !isCheckingOut,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isCheckingOut) "Memproses..." else "Checkout")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun CashierScreenPreview() {
//    WarungKuPOSTheme {
//        TODO()
//    }
//}
