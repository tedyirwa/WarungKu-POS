
package com.teds.warungku.pos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.teds.warungku.pos.data.local.dao.ProductDao
import com.teds.warungku.pos.data.local.dao.TransactionDao
import com.teds.warungku.pos.data.local.entity.ProductEntity
import com.teds.warungku.pos.data.local.entity.TransactionEntity
import com.teds.warungku.pos.data.local.entity.TransactionItemEntity

@Database(
    entities = [
        ProductEntity::class,
        TransactionEntity::class,
        TransactionItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun transactionDao(): TransactionDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE products ADD COLUMN isActive INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date INTEGER NOT NULL,
                        total INTEGER NOT NULL,
                        payment INTEGER NOT NULL,
                        `change` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS transaction_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        transactionId INTEGER NOT NULL,
                        productId INTEGER NOT NULL,
                        productName TEXT NOT NULL,
                        price INTEGER NOT NULL,
                        quantity INTEGER NOT NULL,
                        subtotal INTEGER NOT NULL,
                        FOREIGN KEY(transactionId)
                            REFERENCES transactions(id)
                            ON UPDATE NO ACTION
                            ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_transaction_items_transactionId
                    ON transaction_items(transactionId)
                    """.trimIndent()
                )
            }
        }
    }
}
