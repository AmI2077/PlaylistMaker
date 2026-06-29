package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackCrossRef
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {

    @Query("SELECT * FROM TrackPlaylistEntity")
    fun getAllRelations(): Flow<List<PlaylistTrackCrossRef>>

    @Query("SELECT TracksInPlaylistData.* " +
            "FROM TrackPlaylistEntity tp " +
            "INNER JOIN TracksInPlaylistData ON tp.trackId = TracksInPlaylistData.trackId " +
            "WHERE tp.playlistId = :playlistId")
    fun getTracksFromPlaylist(playlistId: Int): Flow<List<PlaylistTrackDataEntity>>

    @Query("SELECT playlistId FROM TrackPlaylistEntity WHERE trackId = :trackId")
    suspend fun getPlaylistForTrack(trackId: String): List<Int>

    @Insert(onConflict = REPLACE)
    suspend fun insertTrackData(trackDataEntity: PlaylistTrackDataEntity)
    @Insert(onConflict = ABORT)
    suspend fun insertTrackIntoPlaylist(playlistTrackCrossRef: PlaylistTrackCrossRef)
}