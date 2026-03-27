package com.example.playlistmaker.search.ui.states

import com.example.playlistmaker.search.domain.models.Track

sealed class SearchState {
    object Idle: SearchState()
    data class Success(val tracks: List<Track>): SearchState()
    data class Error(val errorMessage: String): SearchState()
    data object EmptyResult: SearchState()
    data object NetworkError: SearchState()
    data object Loading: SearchState()
}