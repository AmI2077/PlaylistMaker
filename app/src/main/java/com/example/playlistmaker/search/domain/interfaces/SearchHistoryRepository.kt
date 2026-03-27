package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun getSearchHistory(): List<Track>
    fun saveSearchHistory(tracks: List<Track>)
    fun clearSearchHistory()
}