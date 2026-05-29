package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.SearchResult
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracksByQuery(query: String): Flow<SearchResult>
}