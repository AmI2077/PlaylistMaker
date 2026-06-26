package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.FavouriteTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteTrackDao {

    @Query("SELECT * FROM FavTracks WHERE trackId = :id")
    suspend fun getTrackFromFavById(id: String): FavouriteTracksEntity?

    @Query("SELECT * FROM FavTracks")
    fun getTracksFromFav(): Flow<List<FavouriteTracksEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertTrackInFav(track: FavouriteTracksEntity)

    @Query("DELETE FROM FavTracks WHERE trackId = :id")
    suspend fun deleteTrackFromFav(id: String)
}