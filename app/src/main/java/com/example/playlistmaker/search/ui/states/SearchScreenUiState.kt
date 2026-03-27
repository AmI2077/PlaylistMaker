package com.example.playlistmaker.search.ui.states

data class SearchScreenUiState(
    val searchState: SearchState,
    val historyState: SearchHistoryState,
    val historyVisibility: Boolean,
)