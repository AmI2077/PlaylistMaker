package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.SearchResult
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    fun searchTracksByQuery(query: String): Flow<SearchResult>
    fun getSearchHistory(): Flow<List<Track>>
    suspend fun addTrackToHistory(track: Track): List<Track>
    suspend fun clearSearchHistory()
}