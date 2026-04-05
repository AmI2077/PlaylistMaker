package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.SearchHistoryState
import com.example.playlistmaker.search.domain.models.SearchState

interface TracksInteractor {

    suspend fun searchTracksByQuery(query: String): SearchState
    suspend fun getSearchHistory(): SearchHistoryState
    suspend fun addTrackToHistory(track: Track): SearchHistoryState
    suspend fun clearSearchHistory(): SearchHistoryState
}