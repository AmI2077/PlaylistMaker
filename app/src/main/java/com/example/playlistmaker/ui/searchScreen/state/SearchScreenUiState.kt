package com.example.playlistmaker.ui.searchScreen.state

data class SearchScreenUiState(
    val searchState: SearchState,
    val historyState: SearchHistoryState,
    val historyVisibility: Boolean,
)