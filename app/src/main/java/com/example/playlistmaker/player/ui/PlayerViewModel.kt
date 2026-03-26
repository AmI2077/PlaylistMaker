package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayer: MediaPlayer
): ViewModel() {
    private var playUrl = ""
    private var isPrepared = false
    private var timerJob: Job? = null

    private var _playTime = MutableLiveData<Int>()
    val playTime: LiveData<Int> get() = _playTime

    private var _playState = MutableLiveData<PlayState>()
    val playState: LiveData<PlayState> get() = _playState

    init {
        initState()
    }

    fun setUrl(url: String) {
        playUrl = url
        if (!isPrepared) prepareMediaPlayer(playUrl)
    }

    fun initState() {
        _playTime.postValue(0)
        _playState.value = PlayState.Idle
    }

    private fun releaseMediaPlayer() {
        mediaPlayer.release()
    }

    private fun prepareMediaPlayer(playUrl: String) {
        mediaPlayer.setDataSource(playUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnCompletionListener {
            initState()
        }
        isPrepared = true
    }

    fun start() {
        timerJob?.cancel()
        mediaPlayer.start()
        _playState.value = PlayState.Play
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                _playTime.postValue(mediaPlayer.currentPosition)
                delay(TIMER_DELAY)
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        mediaPlayer.pause()
        _playState.value = PlayState.Pause
    }

    override fun onCleared() {
        releaseMediaPlayer()
    }

    companion object {
        const val TIMER_DELAY = 500L
    }
}