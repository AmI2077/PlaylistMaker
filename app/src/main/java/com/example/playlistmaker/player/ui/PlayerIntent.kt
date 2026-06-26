package com.example.playlistmaker.player.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface PlayerIntent {
    data class LoadTrack(val id: String, val url: String): PlayerIntent
    object Play: PlayerIntent
    object Pause: PlayerIntent
    data class FavBtnClick(val track: Track): PlayerIntent
}