package com.example.playlistmaker.library.ui.model

import com.example.playlistmaker.library.domain.model.Playlist

data class PlaylistsUiState(
    val playlists: List<Playlist> = emptyList()
)
