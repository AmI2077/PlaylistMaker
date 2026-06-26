package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM Playlist")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM Playlist WHERE id = :playlistId")
    fun getPlaylistDetails(playlistId: Int): Flow<PlaylistEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM Playlist WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)
}