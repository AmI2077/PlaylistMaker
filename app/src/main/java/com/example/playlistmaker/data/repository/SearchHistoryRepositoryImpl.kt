package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.interfaces.SearchHistoryRepository
import com.example.playlistmaker.domain.interfaces.TracksPreferences

class SearchHistoryRepositoryImpl(
    private val tracksPreferences: TracksPreferences
): SearchHistoryRepository {
    override fun getSearchHistory(): List<Track> {
        return tracksPreferences.read().toList()
    }

    override fun saveSearchHistory(tracks: List<Track>) {
        tracksPreferences.write(tracks.toList())
    }

    override fun clearSearchHistory() {
        tracksPreferences.clear()
    }
}