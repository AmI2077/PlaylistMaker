package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.library.data.db.dao.TracksDao
import com.example.playlistmaker.library.domain.api.FavTracksRepository
import com.example.playlistmaker.search.data.extensions.toModel
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavTracksRepositoryImpl(
    private val tracksDao: TracksDao
): FavTracksRepository {
    override fun getTracks(): Flow<List<Track>> {
        return tracksDao.getTracks()
            .map { tracks ->
                tracks.map { it.toModel() }
            }
    }
}