
package com.teds.warungku.pos.presentation.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teds.warungku.pos.presentation.dashboard.viewmodel.DashboardViewModel
import com.teds.warungku.pos.ui.theme.WarungKuPOSTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToProducts: () -> Unit,
    onNavigateToCashier: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToStock: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onNavigateToProducts = onNavigateToProducts,
        onNavigateToCashier = onNavigateToCashier,
        onNavigateToTransactions = onNavigateToTransactions,
        onNavigateToStock = onNavigateToStock
    )
}

@Composable
private fun DashboardContent(
    uiState: com.teds.warungku.pos.presentation.dashboard.viewmodel.DashboardUiState,
    onNavigateToProducts: () -> Unit,
    onNavigateToCashier: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToStock: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            DashboardSummaryCard(
                title = "Omzet Hari Ini",
                value = formatRupiah(uiState.todayRevenue)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Transaksi Hari Ini",
                    value = uiState.todayTransactionCount.toString()
                )

                DashboardSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Produk Aktif",
                    value = uiState.activeProductCount.toString()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            DashboardSummaryCard(
                title = "Stok Menipis",
                value = "${uiState.lowStockProductCount} produk"
            )

            DashboardSummaryCard(
                title = "Stok Habis",
                value = "${uiState.outOfStockProductCount} produk"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardMenuItem("Kasir", onNavigateToCashier)
        DashboardMenuItem("Produk", onNavigateToProducts)
        DashboardMenuItem("Manajemen Stok", onNavigateToStock)
        DashboardMenuItem("Riwayat Transaksi", onNavigateToTransactions)
    }
}

@Composable
private fun DashboardSummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DashboardMenuItem(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        onClick = onClick
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun formatRupiah(amount: Long): String {
    return NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(amount)
}

@Preview(showBackground = true)
@Composable
fun DashboardContentPreview() {
    WarungKuPOSTheme {
        DashboardContent(
            uiState = com.teds.warungku.pos.presentation.dashboard.viewmodel.DashboardUiState(
                todayRevenue = 1250000L,
                todayTransactionCount = 15,
                activeProductCount = 25,
                lowStockProductCount = 3,
                isLoading = false
            ),
            onNavigateToProducts = {},
            onNavigateToCashier = {},
            onNavigateToTransactions = {},
            onNavigateToStock = {}
        )
    }
}
