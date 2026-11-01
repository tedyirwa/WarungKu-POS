package com.teds.warungku.pos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teds.warungku.pos.data.local.database.DatabaseProvider
import com.teds.warungku.pos.data.repository.ProductRepositoryImpl
import com.teds.warungku.pos.data.repository.TransactionRepositoryImpl
import com.teds.warungku.pos.domain.usecase.ActivateProductUseCase
import com.teds.warungku.pos.domain.usecase.AddProductUseCase
import com.teds.warungku.pos.domain.usecase.CheckoutUseCase
import com.teds.warungku.pos.domain.usecase.DeactivateProductUseCase
import com.teds.warungku.pos.domain.usecase.GetActiveProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetAllProductsUseCase
import com.teds.warungku.pos.domain.usecase.GetAllTransactionsUseCase
import com.teds.warungku.pos.domain.usecase.UpdateProductUseCase
import com.teds.warungku.pos.presentation.cashier.screen.CashierScreen
import com.teds.warungku.pos.presentation.cashier.viewmodel.CashierViewModel
import com.teds.warungku.pos.presentation.cashier.viewmodel.CashierViewModelFactory
import com.teds.warungku.pos.presentation.dashboard.screen.DashboardScreen
import com.teds.warungku.pos.presentation.product.screen.ProductScreen
import com.teds.warungku.pos.presentation.product.viewmodel.ProductViewModel
import com.teds.warungku.pos.presentation.product.viewmodel.ProductViewModelFactory
import com.teds.warungku.pos.presentation.transaction.screen.TransactionScreen
import com.teds.warungku.pos.presentation.transaction.viewmodel.TransactionViewModel
import com.teds.warungku.pos.presentation.transaction.viewmodel.TransactionViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val productDao = database.productDao()

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

    val transactionRepository = TransactionRepositoryImpl(database)

    val getAllTransactionsUseCase =
        GetAllTransactionsUseCase(transactionRepository)

    val transactionViewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModelFactory(
            getAllTransactionsUseCase
        )
    )

    val getActiveProductsUseCase =
        GetActiveProductsUseCase(repository)

    val checkoutUseCase =
        CheckoutUseCase(transactionRepository)

    val cashierViewModel: CashierViewModel = viewModel(
        factory = CashierViewModelFactory(
            getActiveProductsUseCase,
            checkoutUseCase
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
                },
                onNavigateToTransactions = {
                    navController.navigate("transactions")
                },
                onNavigateToCashier = {
                    navController.navigate("cashier")
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

        composable("transactions") {
            TransactionScreen(
                viewModel = transactionViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("cashier") {
            CashierScreen(
                viewModel = cashierViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}