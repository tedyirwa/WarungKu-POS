package com.teds.warungku.pos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teds.warungku.pos.data.local.database.DatabaseProvider
import com.teds.warungku.pos.data.repository.ProductRepositoryImpl
import com.teds.warungku.pos.domain.usecase.ActivateProductUseCase
import com.teds.warungku.pos.domain.usecase.AddProductUseCase
import com.teds.warungku.pos.domain.usecase.DeactivateProductUseCase
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import com.teds.warungku.pos.domain.usecase.UpdateProductUseCase
import com.teds.warungku.pos.presentation.dashboard.screen.DashboardScreen
import com.teds.warungku.pos.presentation.product.screen.ProductScreen
import com.teds.warungku.pos.presentation.product.viewmodel.ProductViewModel
import com.teds.warungku.pos.presentation.product.viewmodel.ProductViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val productDao = DatabaseProvider.getDatabase(context).productDao()
    val repository = ProductRepositoryImpl(productDao)

    val getAllProductsUseCase = GetAllProductsUseCase(repository)
    val addProductUseCase = AddProductUseCase(repository)
    val updateProductUseCase = UpdateProductUseCase(repository)
    val deactivateProductUseCase = DeactivateProductUseCase(repository)
    val activateProductUseCase = ActivateProductUseCase(repository)

    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(
            getAllProductsUseCase,
            addProductUseCase,
            updateProductUseCase,
            deactivateProductUseCase,
            activateProductUseCase
        )
    )

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
                },
                viewModel = productViewModel
            )
        }
    }
}