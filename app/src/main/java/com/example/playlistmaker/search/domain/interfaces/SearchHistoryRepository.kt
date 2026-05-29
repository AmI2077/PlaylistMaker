package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<List<Track>>
    suspend fun saveSearchHistory(tracks: List<Track>)
    suspend fun clearSearchHistory()
}