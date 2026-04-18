package com.example.playlistmaker.player.domain

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