package com.teds.warungku.pos.presentation.product.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.teds.warungku.pos.domain.model.Product

@Composable
fun AddProductForm(
    product: Product? = null,
    onSave: (String, Long, Int, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }

    LaunchedEffect(product) {
        name = product?.name ?: ""
        price = product?.price?.toString() ?: ""
        stock = product?.stock?.toString() ?: ""
        unit = product?.unit ?: ""
    }

    val isFormValid =
        name.isNotBlank() &&
                price.toLongOrNull() != null &&
                stock.toIntOrNull() != null &&
                unit.isNotBlank()

    Column {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Produk") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        OutlinedTextField(
            value = price,
            onValueChange = {
                if (it.all(Char::isDigit)) price = it
            },
            label = { Text("Harga (Rp)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = stock,
            onValueChange = {
                if (it.all(Char::isDigit)) stock = it
            },
            label = { Text("Stok") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = unit,
            onValueChange = { unit = it },
            label = { Text("Satuan (kg, pcs, liter)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        Button(
            onClick = {
                onSave(
                    name.trim(),
                    price.toLong(),
                    stock.toInt(),
                    unit.trim()
                )
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }

        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Batal")
        }
    }
}