package com.example.playlistmaker.data.repository.implementation

import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.preferences.SearchHistoryPreferences
import com.example.playlistmaker.data.repository.interfaces.SearchHistoryRepository

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