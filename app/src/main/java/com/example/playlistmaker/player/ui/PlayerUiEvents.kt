package com.example.playlistmaker.player.ui

sealed interface PlayerUiEvents {
    object ShowTrackSuccessAddedMessage: PlayerUiEvents
    object ShowTrackAlreadyExistsMessage: PlayerUiEvents
}