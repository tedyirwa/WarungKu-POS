package com.teds.warungku.pos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teds.warungku.pos.data.local.dao.ProductDao
import com.teds.warungku.pos.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}
