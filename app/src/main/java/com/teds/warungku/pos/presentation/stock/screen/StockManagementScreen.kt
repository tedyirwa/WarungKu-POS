
package com.teds.warungku.pos.presentation.stock.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teds.warungku.pos.domain.model.StockMovement
import com.teds.warungku.pos.presentation.stock.viewmodel.StockManagementViewModel
import com.teds.warungku.pos.presentation.stock.viewmodel.StockManagementViewModelFactory
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun StockManagementScreen(
    stockManagementViewModelFactory: StockManagementViewModelFactory,
    onBack: () -> Unit
) {
    val viewModel: StockManagementViewModel = viewModel(
        factory = stockManagementViewModelFactory
    )

    val products by viewModel.products.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Filter state
    var selectedFilterProductId by remember {
        mutableStateOf<Long?>(null)
    }

    var isFilterExpanded by remember {
        mutableStateOf(false)
    }

    val movementsFlow: Flow<List<StockMovement>> =
        remember(selectedFilterProductId) {
            if (selectedFilterProductId == null) {
                viewModel.stockMovements
            } else {
                viewModel.getMovementsByProduct(
                    selectedFilterProductId!!
                )
            }
        }

    val stockMovements by movementsFlow.collectAsState(
        initial = emptyList()
    )

    // Stock In state
    var stockInProductId by remember {
        mutableStateOf<Long?>(null)
    }

    var stockInQuantity by remember {
        mutableStateOf("")
    }

    // Stock Adjustment state
    var adjustmentProductId by remember {
        mutableStateOf<Long?>(null)
    }

    var adjustmentQuantity by remember {
        mutableStateOf("")
    }

    var adjustmentType by remember {
        mutableStateOf("INCREASE")
    }

    // Snackbar
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(
        uiState.successMessage,
        uiState.errorMessage
    ) {
        if (uiState.successMessage != null) {
            stockInProductId = null
            adjustmentProductId = null
        }

        val message = uiState.successMessage ?: uiState.errorMessage

        if (message != null) {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Manajemen Stok",
                    style = MaterialTheme.typography.headlineMedium
                )

                TextButton(onClick = onBack) {
                    Text("Kembali")
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Product section
                item {
                    Text(
                        text = "Daftar Produk",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                if (products.isEmpty()) {
                    item {
                        Text("Belum ada produk.")
                    }
                }

                items(
                    items = products,
                    key = { "product_${it.id}" }
                ) { product ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Stok: ${product.stock} ${product.unit}"
                            )

                            Text(
                                text = when {
                                    product.stock == 0 -> "Habis"
                                    product.stock <= 5 -> "Stok Menipis"
                                    else -> "Stok Aman"
                                },
                                color = when {
                                    product.stock == 0 ->
                                        MaterialTheme.colorScheme.error

                                    product.stock <= 5 ->
                                        MaterialTheme.colorScheme.tertiary

                                    else ->
                                        MaterialTheme.colorScheme.primary
                                },
                                style = MaterialTheme.typography.labelMedium
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        stockInProductId = product.id
                                        stockInQuantity = ""
                                    }
                                ) {
                                    Text("Stock In")
                                }

                                OutlinedButton(
                                    onClick = {
                                        adjustmentProductId = product.id
                                        adjustmentQuantity = ""
                                        adjustmentType = "INCREASE"
                                    }
                                ) {
                                    Text("Adjustment")
                                }
                            }
                        }
                    }
                }

                // Stock movement section
                item {
                    Text(
                        text = "Riwayat Perubahan Stok",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                item {
                    OutlinedButton(
                        onClick = {
                            isFilterExpanded = true
                        }
                    ) {
                        Text(
                            text = selectedFilterProductId?.let { id ->
                                products.find { it.id == id }?.name
                                    ?: "Produk"
                            } ?: "Semua Produk"
                        )
                    }

                    DropdownMenu(
                        expanded = isFilterExpanded,
                        onDismissRequest = {
                            isFilterExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Semua Produk")
                            },
                            onClick = {
                                selectedFilterProductId = null
                                isFilterExpanded = false
                            }
                        )

                        products.forEach { product ->
                            DropdownMenuItem(
                                text = {
                                    Text(product.name)
                                },
                                onClick = {
                                    selectedFilterProductId = product.id
                                    isFilterExpanded = false
                                }
                            )
                        }
                    }
                }

                if (stockMovements.isEmpty()) {
                    item {
                        Text("Belum ada riwayat perubahan stok.")
                    }
                }

                items(
                    items = stockMovements,
                    key = { "movement_${it.id}" }
                ) { movement ->

                    val date = remember(movement.createdAt) {
                        SimpleDateFormat(
                            "dd MMM yyyy, HH:mm",
                            Locale("id", "ID")
                        ).format(Date(movement.createdAt))
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = movement.productName,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = when (movement.type) {
                                    "IN" -> "Stok Masuk"
                                    "ADJUSTMENT" -> "Penyesuaian Stok"
                                    "SALE" -> "Penjualan"
                                    else -> movement.type
                                }
                            )

                            Text(
                                text = "Perubahan: ${
                                    if (movement.quantityChange > 0) "+" else ""
                                }${movement.quantityChange}"
                            )

                            Text(
                                text = "Stok: ${movement.stockBefore} → ${movement.stockAfter}"
                            )

                            Text(
                                text = date,
                                style = MaterialTheme.typography.bodySmall
                            )

                            movement.note?.let { note ->
                                Text(
                                    text = "Catatan: $note",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Stock In dialog
    if (stockInProductId != null) {
        AlertDialog(
            onDismissRequest = {
                stockInProductId = null
            },
            title = {
                Text("Tambah Stok")
            },
            text = {
                OutlinedTextField(
                    value = stockInQuantity,
                    onValueChange = {
                        stockInQuantity = it
                    },
                    label = {
                        Text("Jumlah Stok")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            },
            confirmButton = {
                Button(
                    enabled = !uiState.isLoading &&
                            (stockInQuantity.toIntOrNull() ?: 0) > 0,
                    onClick = {
                        val quantity = stockInQuantity.toIntOrNull()

                        if (quantity != null) {
                            viewModel.stockIn(
                                productId = stockInProductId!!,
                                quantity = quantity,
                                note = null
                            )
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        stockInProductId = null
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    // Stock Adjustment dialog
    if (adjustmentProductId != null) {
        AlertDialog(
            onDismissRequest = {
                adjustmentProductId = null
            },
            title = {
                Text("Stock Adjustment")
            },
            text = {
                Column {
                    Text("Pilih jenis penyesuaian")

                    Row {
                        TextButton(
                            onClick = {
                                adjustmentType = "INCREASE"
                            }
                        ) {
                            Text(
                                if (adjustmentType == "INCREASE") {
                                    "✓ Tambah"
                                } else {
                                    "Tambah"
                                }
                            )
                        }

                        TextButton(
                            onClick = {
                                adjustmentType = "DECREASE"
                            }
                        ) {
                            Text(
                                if (adjustmentType == "DECREASE") {
                                    "✓ Kurangi"
                                } else {
                                    "Kurangi"
                                }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = adjustmentQuantity,
                        onValueChange = {
                            adjustmentQuantity = it
                        },
                        label = {
                            Text("Jumlah perubahan")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = !uiState.isLoading &&
                            (adjustmentQuantity.toIntOrNull() ?: 0) > 0,
                    onClick = {
                        val quantity = adjustmentQuantity.toIntOrNull()

                        if (quantity != null) {
                            viewModel.adjustStock(
                                productId = adjustmentProductId!!,
                                quantityChange = if (
                                    adjustmentType == "INCREASE"
                                ) {
                                    quantity
                                } else {
                                    -quantity
                                },
                                note = "Stock adjustment"
                            )

                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        adjustmentProductId = null
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}
