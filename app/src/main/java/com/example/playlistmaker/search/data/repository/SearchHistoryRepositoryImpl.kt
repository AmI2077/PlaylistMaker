package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.local.StorageClient
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.interfaces.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val storageClient: StorageClient<List<Track>>
): SearchHistoryRepository {
    override fun getSearchHistory(): Flow<List<Track>> = flow {
        emit(storageClient.getData())
    }

    override suspend fun saveSearchHistory(tracks: List<Track>) {
        storageClient.storeData(tracks)
    }

    override suspend fun clearSearchHistory() {
        storageClient.clearData()
    }
}