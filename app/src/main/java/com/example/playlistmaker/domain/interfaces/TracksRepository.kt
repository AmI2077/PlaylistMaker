package com.example.playlistmaker.domain.interfaces

import com.example.playlistmaker.domain.models.SearchResult

interface TracksRepository {
    suspend fun searchTracksByQuery(query: String): SearchResult
}