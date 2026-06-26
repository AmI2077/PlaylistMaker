package com.example.playlistmaker.player.domain.api

interface AudioPlayer {

    fun preparePlayer(
        playUrl: String,
        onCompletion: () -> Unit
    )
    fun getCurrentPosition(): Int
    fun play()
    fun pause()
    fun close()
}