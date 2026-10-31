package com.teds.warungku.pos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.teds.warungku.pos.data.local.dao.ProductDao
import com.teds.warungku.pos.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE products ADD COLUMN isActive INTEGER NOT NULL DEFAULT 1"
                )
            }
        }
    }
    abstract fun productDao(): ProductDao
}
