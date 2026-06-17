package com.example.playlistmaker.library.ui.model

import com.example.playlistmaker.search.domain.models.Track

data class LibraryUiState(
    val tracks: List<Track> = emptyList()
)