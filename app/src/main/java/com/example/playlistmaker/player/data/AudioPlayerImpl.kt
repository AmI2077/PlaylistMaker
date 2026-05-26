package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.AudioPlayer

class AudioPlayerImpl(
    private val mediaPlayer: MediaPlayer,
): AudioPlayer {

    override fun preparePlayer(
        playUrl: String,
        onCompletion: () -> Unit
    ) {
        mediaPlayer.apply {
            mediaPlayer.reset()
            setDataSource(playUrl)
            prepareAsync()
            setOnCompletionListener {
                onCompletion()
            }
        }
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun close() {
        mediaPlayer.reset()
    }

}