package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.interfaces.SearchHistoryRepository
import com.example.playlistmaker.search.domain.interfaces.TracksInteractor
import com.example.playlistmaker.search.domain.interfaces.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResult
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): TracksInteractor {
    override fun searchTracksByQuery(query: String): Flow<SearchResult> {
        return tracksRepository.searchTracksByQuery(query)
    }

    override fun getSearchHistory(): Flow<List<Track>> {
        return searchHistoryRepository.getSearchHistory()
    }

    override suspend fun addTrackToHistory(track: Track): List<Track> {
        var currentHistory = mutableListOf<Track>()
        searchHistoryRepository.getSearchHistory()
            .collect {
                currentHistory = it.toMutableList()
            }

        val iterator = currentHistory.iterator()
        var index: Int = -1
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.trackId == track.trackId) {
                index = currentHistory.indexOf(item)
                iterator.remove()
                break
            }
        }
        if (currentHistory.size >= 10) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }
        currentHistory.add(0, track)
        searchHistoryRepository.saveSearchHistory(currentHistory)

        return currentHistory
    }

    override suspend fun clearSearchHistory() {
        searchHistoryRepository.clearSearchHistory()
    }
}