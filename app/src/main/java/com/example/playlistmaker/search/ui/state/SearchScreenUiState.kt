package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.models.Track

data class SearchScreenUiState(
    val historyVisible: Boolean = false,
    val userQuery: String = "",
    val searchedTracks: List<Track>? = null,
    val historyTracks: List<Track>? = null,
    val error: SearchError? = null,
    val loading: Boolean = false
)

sealed interface SearchError {
    object NetworkError: SearchError
    object EmptyResult: SearchError
}