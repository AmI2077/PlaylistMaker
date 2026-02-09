package com.example.playlistmaker.ui.playerScreen.state

sealed interface PlayState {
    data object Play: PlayState
    data object Pause: PlayState
    data object Idle: PlayState
}