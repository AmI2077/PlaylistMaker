package com.example.playlistmaker.player.ui

data class PlayerUiState(
    val isPlaying: Boolean = false,
    val isLiked: Boolean = false,
    val playTime: Int = 0
)
