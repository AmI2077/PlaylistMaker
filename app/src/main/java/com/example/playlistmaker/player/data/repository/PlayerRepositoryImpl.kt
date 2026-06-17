package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.library.data.db.dao.TracksDao
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.search.data.extensions.toEntity
import com.example.playlistmaker.search.data.extensions.toModel
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(
    private val tracksDao: TracksDao
): PlayerRepository {
    override suspend fun getTrack(id: String): Track? =
        withContext(Dispatchers.IO) {
            tracksDao.getTrackById(id)?.toModel()
        }

    override suspend fun addTrackToFav(track: Track) {
        tracksDao.insertTrack(track.toEntity())
    }

    override suspend fun deleteTrackFromFav(id: String) {
        tracksDao.deleteTrack(id)
    }
}


