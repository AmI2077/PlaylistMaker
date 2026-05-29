package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.AudioPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val audioPlayer: AudioPlayer
): ViewModel() {
    private var playUrl = ""
    private var timerJob: Job? = null

    private var _playTime = MutableLiveData<Int>()
    val playTime: LiveData<Int> = _playTime

    private var _playState = MutableLiveData<PlayState>()
    val playState: LiveData<PlayState> = _playState

    init {
        initState()
    }

    fun setUrl(url: String) {
        playUrl = url
        prepareMediaPlayer(playUrl)
    }

    fun initState() {
        timerJob?.cancel()
        _playTime.value = 0
        _playState.value = PlayState.Idle
    }

    private fun closePlayer() {
        timerJob?.cancel()
        audioPlayer.close()
    }

    private fun prepareMediaPlayer(playUrl: String) {
        audioPlayer.preparePlayer(playUrl) {
            initState()
        }
    }

    fun start() {
        audioPlayer.play()
        _playState.value = PlayState.Play
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(TIMER_DELAY)
                _playTime.value = audioPlayer.getCurrentPosition()
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        audioPlayer.pause()
        _playState.value = PlayState.Pause
    }

    override fun onCleared() {
        closePlayer()
    }

    companion object {
        const val TIMER_DELAY = 300L
    }
}