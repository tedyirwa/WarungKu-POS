package com.teds.warungku.pos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teds.warungku.pos.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET isActive = 1 WHERE id = :productId")
    suspend fun activateProduct(productId: Long)


    @Query("UPDATE products SET isActive = 0 WHERE id = :productId")
    suspend fun deactivateProduct(productId: Long)
}