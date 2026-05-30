package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<List<Track>>
    fun saveSearchHistory(tracks: List<Track>)
    fun clearSearchHistory()
}