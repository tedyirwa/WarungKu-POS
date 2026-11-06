package com.teds.warungku.pos.domain.repository

import com.teds.warungku.pos.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getAllProducts(): Flow<List<Product>>

    fun getActiveProducts(): Flow<List<Product>>

    suspend fun addProduct(product: Product)

    suspend fun updateProduct(product: Product)

    suspend fun deactivateProduct(productId: Long)

    suspend fun activateProduct(productId: Long)

    fun getLowStockProductCount(): Flow<Int>

    fun getOutOfStockProductCount(): Flow<Int>

}