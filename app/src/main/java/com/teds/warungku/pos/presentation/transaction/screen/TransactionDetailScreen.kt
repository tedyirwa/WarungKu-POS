package com.teds.warungku.pos.presentation.transaction.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teds.warungku.pos.presentation.transaction.viewmodel.TransactionDetailViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel,
    onBack: () -> Unit
) {
    val transaction by viewModel.transaction.collectAsState()
    val transactionItems by viewModel.transactionItems.collectAsState()

    val currencyFormat = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    ).apply {
        maximumFractionDigits = 0
    }

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
                text = "Detail Transaksi",
                style = MaterialTheme.typography.headlineSmall
            )

            TextButton(onClick = onBack) {
                Text("Kembali")
            }
        }

        transaction?.let {
            Text(text = "Transaksi #${it.id}")
            Text(text = "Total: ${currencyFormat.format(it.total)}")
            Text(text = "Pembayaran: ${it.payment}")
            Text(text = "Kembalian: ${it.change}")
            Text(
                text = SimpleDateFormat(
                    "dd MMM yyyy, HH:mm",
                    Locale("id", "ID")
                ).format(Date(it.date))
            )
        }

        transactionItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.productName)
                Text(text = currencyFormat.format(item.subtotal))
            }

            Text(
                text = "${item.quantity} x ${currencyFormat.format(item.price)}",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider()
        }
    }
}