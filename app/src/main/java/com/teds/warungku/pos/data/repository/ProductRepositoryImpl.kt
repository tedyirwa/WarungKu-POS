package com.teds.warungku.pos.data.repository

import com.teds.warungku.pos.data.local.dao.ProductDao
import com.teds.warungku.pos.data.local.entity.ProductEntity
import com.teds.warungku.pos.domain.model.Product
import com.teds.warungku.pos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { entity ->
                Product(
                    id = entity.id,
                    name = entity.name,
                    price = entity.price,
                    stock = entity.stock,
                    unit = entity.unit,
                    isActive = entity.isActive
                )
            }
        }
    }

    override suspend fun addProduct(product: Product) {
        val entity = ProductEntity(
            id = product.id,
            name = product.name,
            price = product.price,
            stock = product.stock,
            unit = product.unit,
            isActive = product.isActive
        )

        productDao.insertProduct(entity)
    }

    override suspend fun updateProduct(product: Product) {
        val entity = ProductEntity(
            id = product.id,
            name = product.name,
            price = product.price,
            stock = product.stock,
            unit = product.unit,
            isActive = product.isActive
        )

        productDao.updateProduct(entity)
    }

    override suspend fun deactivateProduct(productId: Long) {
        productDao.deactivateProduct(productId)
    }

    override suspend fun activateProduct(productId: Long) {
        productDao.activateProduct(productId)
    }

}