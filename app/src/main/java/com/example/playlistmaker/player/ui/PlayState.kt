package com.example.playlistmaker.player.ui

sealed interface PlayState {
    data object Play: PlayState
    data object Pause: PlayState
    data object Idle: PlayState
}