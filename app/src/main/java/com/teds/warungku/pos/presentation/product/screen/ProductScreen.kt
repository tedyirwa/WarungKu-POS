package com.teds.warungku.pos.presentation.product.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.presentation.product.component.AddProductForm
import com.teds.warungku.pos.presentation.product.viewmodel.ProductViewModel
import com.teds.warungku.pos.ui.theme.WarungKuPOSTheme

@Composable
fun ProductScreen(
    onBack: () -> Unit,
    viewModel: ProductViewModel
) {
    val products by viewModel.products.collectAsState()

    ProductContent(
        products = products,
        onBack = onBack,
        onAddProduct = { name, price, stock, unit ->
            viewModel.addProduct(
                Product(
                    id = 0,
                    name = name,
                    price = price,
                    stock = stock,
                    unit = unit
                )
            )
        },
        onUpdateProduct = { product ->
            viewModel.updateProduct(product)
        },
        onDeactivateProduct = { productId ->
            viewModel.deactivateProduct(productId)
        },
        onActivateProduct = { productId ->
            viewModel.activateProduct(productId)
        }
    )
}

@Composable
private fun ProductContent(
    products: List<Product>,
    onBack: () -> Unit,
    onAddProduct: (String, Long, Int, String) -> Unit,
    onUpdateProduct: (Product) -> Unit,
    onDeactivateProduct: (Long) -> Unit,
    onActivateProduct: (Long) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Daftar Produk")
        Text("Jumlah produk: ${products.size}")

        Button(onClick = onBack) {
            Text("Kembali")
        }

        Button(
            onClick = {
                editingProduct = null
                showForm = true
            }
        ) {
            Text("Tambah Produk")
        }

        if (showForm) {
            AddProductForm(
                product = editingProduct,
                onSave = { name, price, stock, unit ->
                    val selectedProduct = editingProduct

                    if (selectedProduct == null) {
                        onAddProduct(name, price, stock, unit)
                    } else {
                        onUpdateProduct(
                            selectedProduct.copy(
                                name = name,
                                price = price,
                                stock = stock,
                                unit = unit
                            )
                        )
                    }

                    showForm = false
                    editingProduct = null
                },
                onCancel = {
                    showForm = false
                    editingProduct = null
                }
            )
        }

        if (products.isEmpty()) {
            Text("Belum ada produk")
        } else {
            LazyColumn {
                items(
                    items = products,
                    key = { it.id }
                ) { product ->
                    Column {
                        Text(product.name)
                        Text("Harga: Rp${product.price}")
                        Text("Stok: ${product.stock} ${product.unit}")
                        Text(
                            if (product.isActive) "Status: Aktif"
                            else "Status: Nonaktif"
                        )
                        Button(
                            onClick = {
                                editingProduct = product
                                showForm = true
                            }
                        ) {
                            Text("Edit")
                        }
                        if (product.isActive) {
                            Button(
                                onClick = { onDeactivateProduct(product.id) }
                            ) {
                                Text("Nonaktifkan")
                            }
                        } else {
                            Button(
                                onClick = { onActivateProduct(product.id) }
                            ) {
                                Text("Aktifkan")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductScreenPreview() {
    val dummyProducts = listOf(
        Product(
            id = 1,
            name = "Beras",
            price = 15000,
            stock = 20,
            unit = "kg"
        ),
        Product(
            id = 2,
            name = "Minyak Goreng",
            price = 18000,
            stock = 10,
            unit = "liter"
        )
    )

    WarungKuPOSTheme {
        ProductContent(
            products = dummyProducts,
            onBack = {},
            onAddProduct = { _, _, _, _ -> },
            onUpdateProduct = {},
            onActivateProduct = {},
            onDeactivateProduct = {}
        )
    }
}