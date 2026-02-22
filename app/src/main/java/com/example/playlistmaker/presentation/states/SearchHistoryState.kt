package com.example.playlistmaker.presentation.states

import com.example.playlistmaker.domain.models.Track

sealed class SearchHistoryState {
    data class History(val searchedTracks: List<Track>): SearchHistoryState()
    data object EmptyHistory: SearchHistoryState()
}