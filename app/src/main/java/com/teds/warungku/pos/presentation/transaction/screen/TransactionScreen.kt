package com.teds.warungku.pos.presentation.transaction.screen

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teds.warungku.pos.presentation.transaction.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    onBack: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()

    val currencyFormat = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    val dateFormat = SimpleDateFormat(
        "dd MMM yyyy, HH:mm",
        Locale("id", "ID")
    )

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
                text = "Riwayat Transaksi",
                style = MaterialTheme.typography.headlineSmall
            )

            TextButton(onClick = onBack) {
                Text("Kembali")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text("Belum ada transaksi.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = transactions,
                    key = { it.id }
                ) { transaction ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Transaksi #${transaction.id}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = dateFormat.format(
                                    Date(transaction.date)
                                )
                            )

                            Text("Total: ${currencyFormat.format(transaction.total)}")
                            Text("Pembayaran: ${currencyFormat.format(transaction.payment)}")
                            Text("Kembalian: ${currencyFormat.format(transaction.change)}")
                        }
                    }
                }
            }
        }
    }
}