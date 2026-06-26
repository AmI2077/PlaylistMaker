package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {

    @Query("SELECT * FROM Track WHERE trackId = :id")
    suspend fun getTrackById(id: String): TrackEntity?

    @Query("SELECT * FROM Track")
    fun getTracks(): Flow<List<TrackEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("DELETE FROM Track WHERE trackId = :id")
    suspend fun deleteTrack(id: String)
}