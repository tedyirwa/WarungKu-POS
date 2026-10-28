
package com.teds.warungku.pos.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: ProductDatabase? = null

    fun getDatabase(context: Context): ProductDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                ProductDatabase::class.java,
                "warungku_database"
            ).build().also { INSTANCE = it }
        }
    }
}
