// kotlin
// Arquivo: app/src/main/java/com/example/myapplication/MyApplication.kt
package com.example.myapplication

import android.app.Application
import androidx.room.Room
import com.example.myapplication.data.local.AppDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
