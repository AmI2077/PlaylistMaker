package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.preferences.SearchHistoryPreferences

class SearchHistoryRepositoryImpl(
    private val searchHistoryPreferences: SearchHistoryPreferences
): SearchHistoryRepository {
    override fun get(): List<Track> {
        return searchHistoryPreferences.read().toList()
    }

    override fun save(tracks: List<Track>) {
        searchHistoryPreferences.write(tracks.toList())
    }

    override fun clear() {
        searchHistoryPreferences.clear()
    }
}