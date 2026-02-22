package com.example.playlistmaker.presentation.states

sealed interface PlayState {
    data object Play: PlayState
    data object Pause: PlayState
    data object Idle: PlayState
}