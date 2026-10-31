package com.teds.warungku.pos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teds.warungku.pos.presentation.dashboard.screen.DashboardScreen
import com.teds.warungku.pos.presentation.product.screen.ProductScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(
                onNavigateToProducts = {
                    navController.navigate("products")
                }
            )
        }

        composable("products") {
            ProductScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}