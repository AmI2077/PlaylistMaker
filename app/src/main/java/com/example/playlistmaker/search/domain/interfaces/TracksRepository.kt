package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.SearchResult

interface TracksRepository {
    suspend fun searchTracksByQuery(query: String): SearchResult
}