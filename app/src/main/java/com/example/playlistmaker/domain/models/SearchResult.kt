package com.example.playlistmaker.domain.models

sealed class SearchResult() {
    class Success(val tracks: List<Track>): SearchResult()
    object Empty: SearchResult()
    object NetworkError: SearchResult()
}