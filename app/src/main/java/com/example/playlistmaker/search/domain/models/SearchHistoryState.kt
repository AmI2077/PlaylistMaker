package com.example.playlistmaker.search.domain.models

sealed class SearchHistoryState {
    data class History(val searchedTracks: List<Track>): SearchHistoryState()
    data object EmptyHistory: SearchHistoryState()
}