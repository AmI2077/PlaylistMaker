package com.example.playlistmaker.library.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.PlaylistTrackDao
import com.example.playlistmaker.library.data.db.dao.FavouriteTrackDao
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.FavouriteTracksEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackCrossRef
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackDataEntity

@Database(entities = [FavouriteTracksEntity::class, PlaylistTrackCrossRef::class, PlaylistEntity::class,
    PlaylistTrackDataEntity::class], version = 9)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTracksDao(): FavouriteTrackDao
    abstract fun getPlaylistsDao(): PlaylistDao
    abstract fun getTrackPlaylistDao(): PlaylistTrackDao

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
                .fallbackToDestructiveMigration(true)
                .build()
            INSTANCE = instance
            instance
        }
    }
}