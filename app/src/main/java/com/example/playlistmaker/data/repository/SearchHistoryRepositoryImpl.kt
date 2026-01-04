package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.preferences.SearchHistoryPreferences

class SearchHistoryRepositoryImpl(
    private val searchHistoryPreferences: SearchHistoryPreferences
): SearchHistoryRepository {
    override fun get(): List<Track> {
        return searchHistoryPreferences.read().toList()
    }

    override fun save(tracks: List<Track>) {
        searchHistoryPreferences.write(tracks.toTypedArray())
    }

    override fun clear() {
        searchHistoryPreferences.clear()
    }
}