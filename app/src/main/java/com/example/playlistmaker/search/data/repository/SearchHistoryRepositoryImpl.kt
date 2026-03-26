package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.local.StorageClient
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.interfaces.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val storageClient: StorageClient<List<Track>>
): SearchHistoryRepository {
    override fun getSearchHistory(): List<Track> {
        return storageClient.getData()
    }

    override fun saveSearchHistory(tracks: List<Track>) {
        storageClient.storeData(tracks)
    }

    override fun clearSearchHistory() {
        storageClient.clearData()
    }
}