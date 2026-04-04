package com.example.playlistmaker.search.domain.models

data class SearchScreenUiState(
    val searchState: SearchState,
    val historyState: SearchHistoryState,
    val historyVisibility: Boolean,
)