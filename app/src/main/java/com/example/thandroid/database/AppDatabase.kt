package com.example.thandroid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thandroid.model.Work

@Database(entities = [Work::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workDao() : WorkDao

    companion object {
        const val DATABASE_NAME = "work_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}