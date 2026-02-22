package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interfaces.SearchHistoryRepository
import com.example.playlistmaker.domain.interfaces.TracksInteractor
import com.example.playlistmaker.domain.interfaces.TracksRepository
import com.example.playlistmaker.domain.models.SearchResult
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.states.SearchHistoryState
import com.example.playlistmaker.presentation.states.SearchState

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): TracksInteractor {
    override suspend fun searchTracksByQuery(query: String): SearchState {
        val result = tracksRepository.searchTracksByQuery(query)
        return when(result) {
            SearchResult.Empty -> SearchState.EmptyResult
            SearchResult.NetworkError -> SearchState.NetworkError
            is SearchResult.Success -> SearchState.Success(result.tracks)
        }
    }

    override suspend fun getSearchHistory(): SearchHistoryState {
        val result = searchHistoryRepository.getSearchHistory()

        return if (result.isEmpty()) {
            SearchHistoryState.EmptyHistory
        } else {
            SearchHistoryState.History(result)
        }
    }

    override suspend fun addTrackToHistory(track: Track): SearchHistoryState {
        val currentHistory = searchHistoryRepository.getSearchHistory().toMutableList()

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

        return SearchHistoryState.History(currentHistory)
    }

    override suspend fun clearSearchHistory(): SearchHistoryState {
        searchHistoryRepository.clearSearchHistory()

        return SearchHistoryState.EmptyHistory
    }
}