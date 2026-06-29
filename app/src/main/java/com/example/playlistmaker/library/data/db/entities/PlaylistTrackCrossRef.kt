package com.example.playlistmaker.library.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "TrackPlaylistEntity",
    primaryKeys = ["trackId", "playlistId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistTrackDataEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"]
        ),
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = CASCADE
        ),
    ]
)
data class PlaylistTrackCrossRef(
    val trackId: String,
    val playlistId: Int,
)
