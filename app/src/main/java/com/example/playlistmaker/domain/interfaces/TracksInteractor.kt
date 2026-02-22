package com.example.playlistmaker.domain.interfaces

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.states.SearchHistoryState
import com.example.playlistmaker.presentation.states.SearchState

interface TracksInteractor {

    suspend fun searchTracksByQuery(query: String): SearchState
    suspend fun getSearchHistory(): SearchHistoryState
    suspend fun addTrackToHistory(track: Track): SearchHistoryState
    suspend fun clearSearchHistory(): SearchHistoryState
}