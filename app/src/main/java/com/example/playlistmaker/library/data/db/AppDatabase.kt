package com.example.playlistmaker.library.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.TracksDao
import com.example.playlistmaker.library.data.db.entities.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTracksDao(): TracksDao

    companion object {
        private const val DATABASE_NAME = "AppDatabase"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room
                .databaseBuilder<AppDatabase>(
                    context = context,
                    name = DATABASE_NAME,
                )
                .build()
            INSTANCE = instance
            instance
        }
    }
}