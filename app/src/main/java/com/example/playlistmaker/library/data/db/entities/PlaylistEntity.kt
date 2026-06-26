package com.example.playlistmaker.library.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String? = null,
    val createDate: String,
    val artWorkUri: String? = null,
)