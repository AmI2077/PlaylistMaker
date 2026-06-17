package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksRepository {

    fun getTracks(): Flow<List<Track>>
}