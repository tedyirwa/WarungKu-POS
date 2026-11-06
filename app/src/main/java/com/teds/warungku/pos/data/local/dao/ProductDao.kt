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

    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveProducts(): Flow<List<ProductEntity>>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET isActive = 1 WHERE id = :productId")
    suspend fun activateProduct(productId: Long)


    @Query("UPDATE products SET isActive = 0 WHERE id = :productId")
    suspend fun deactivateProduct(productId: Long)

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Long): ProductEntity?

    @Query(
        """
    UPDATE products
    SET stock = stock - :quantity
    WHERE id = :productId
    AND isActive = 1
    AND stock >= :quantity
"""
    )
    suspend fun decreaseStock(
        productId: Long,
        quantity: Int
    ): Int

    @Query("""
    UPDATE products
    SET stock = stock + :quantityChange
    WHERE id = :productId
    AND stock + :quantityChange >= 0
""")
    suspend fun adjustStock(
        productId: Long,
        quantityChange: Int
    ): Int

    @Query(
        """
    SELECT COUNT(*)
    FROM products
    WHERE isActive = 1
    AND stock BETWEEN 1 AND 5
    """
    )
    fun getLowStockProductCount(): Flow<Int>

    @Query("""
    SELECT COUNT(*)
    FROM products
    WHERE isActive = 1
    AND stock = 0
""")
    fun getOutOfStockProductCount(): Flow<Int>
}