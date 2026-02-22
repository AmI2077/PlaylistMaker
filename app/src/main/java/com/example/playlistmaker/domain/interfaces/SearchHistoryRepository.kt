package com.example.playlistmaker.domain.interfaces

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getSearchHistory(): List<Track>
    fun saveSearchHistory(tracks: List<Track>)
    fun clearSearchHistory()
}