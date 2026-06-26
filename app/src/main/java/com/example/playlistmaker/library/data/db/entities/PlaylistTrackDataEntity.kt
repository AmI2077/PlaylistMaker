package com.example.playlistmaker.library.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TracksInPlaylistData")
data class PlaylistTrackDataEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val previewUrl: String,
)
