package com.example.playlistmaker.presentation.states

data class SearchScreenUiState(
    val searchState: SearchState,
    val historyState: SearchHistoryState,
    val historyVisibility: Boolean,
)