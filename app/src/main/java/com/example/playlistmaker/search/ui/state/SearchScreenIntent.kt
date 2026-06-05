package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.models.Track

sealed interface SearchScreenIntent {
    data class Search(val query: String): SearchScreenIntent
    data class TrackClick(val track: Track): SearchScreenIntent
    object ClearSearch: SearchScreenIntent
    object ClearHistory: SearchScreenIntent
    object RefreshSearch: SearchScreenIntent
}