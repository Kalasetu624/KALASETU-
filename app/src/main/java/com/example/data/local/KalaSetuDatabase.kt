package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CatalogProductEntity::class, ArtisanOrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KalaSetuDatabase : RoomDatabase() {
    abstract fun kalaSetuDao(): KalaSetuDao

    companion object {
        @Volatile
        private var INSTANCE: KalaSetuDatabase? = null

        fun getInstance(context: Context): KalaSetuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KalaSetuDatabase::class.java,
                    "kalasetu_artisan_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
