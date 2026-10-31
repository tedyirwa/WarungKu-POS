package com.teds.warungku.pos.presentation.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teds.warungku.pos.ui.theme.WarungKuPOSTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigateToProducts: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "WarungKu POS",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = LocalDate.now().format(
                DateTimeFormatter.ofPattern(
                    "EEEE, dd MMMM yyyy",
                    Locale("id", "ID")
                )
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                title = "Penjualan Hari Ini",
                value = "Rp 1.250.000",
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Transaksi Hari Ini",
                value = "15",
                modifier = Modifier.weight(1f)
            )
        }

        SummaryCard(
            title = "Jumlah Produk",
            value = "25",
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Perhatian Stok",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Stok Menipis",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "3 produk perlu segera ditambah stoknya"
                )
            }
        }

        OutlinedButton(
            onClick = onNavigateToProducts,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kelola Produk")
        }

    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    WarungKuPOSTheme {
        DashboardScreen(
            onNavigateToProducts = {}
        )
    }
}