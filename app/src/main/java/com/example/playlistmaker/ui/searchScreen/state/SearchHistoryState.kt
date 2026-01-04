package com.example.playlistmaker.ui.searchScreen.state

import com.example.playlistmaker.data.models.Track

sealed class SearchHistoryState {
    data class History(val searchedTracks: List<Track>): SearchHistoryState()
    data object EmptyHistory: SearchHistoryState()
}