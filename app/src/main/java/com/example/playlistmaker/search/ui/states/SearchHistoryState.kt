package com.example.playlistmaker.search.ui.states

import com.example.playlistmaker.search.domain.models.Track

sealed class SearchHistoryState {
    data class History(val searchedTracks: List<Track>): SearchHistoryState()
    data object EmptyHistory: SearchHistoryState()
}