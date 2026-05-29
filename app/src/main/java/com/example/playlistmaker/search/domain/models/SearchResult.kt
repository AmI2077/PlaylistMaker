package com.example.playlistmaker.search.domain.models

sealed interface SearchResult {
    class Success(val tracks: List<Track>): SearchResult
    object Empty: SearchResult
    object NetworkError: SearchResult
}