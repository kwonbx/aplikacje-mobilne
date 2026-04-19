package com.example.szlaki

import android.content.Context
import androidx.room.*

@Database(entities = [TrailEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun trailDao(): TrailDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "szlaki_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}