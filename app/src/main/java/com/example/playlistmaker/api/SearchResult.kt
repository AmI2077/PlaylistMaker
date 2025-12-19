package com.example.playlistmaker.api

import com.example.playlistmaker.models.Track

sealed class SearchResult {

    data class Success(val tracks: MutableList<Track>): SearchResult()
    class Error(val errorMessage: String): SearchResult()
    class EmptyResult(): SearchResult()
    class NetworkError(): SearchResult()
}
