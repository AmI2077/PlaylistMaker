package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.AudioPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val audioPlayer: AudioPlayer
): ViewModel() {
    private var playUrl = ""
    private var timerJob: Job? = null
    private var isPlaying: Boolean = false

    private var _playTime = MutableLiveData<Int>()
    val playTime: LiveData<Int> get() = _playTime

    private var _playState = MutableLiveData<PlayState>()
    val playState: LiveData<PlayState> get() = _playState

    init {
        initState()
    }

    fun setUrl(url: String) {
        playUrl = url
        prepareMediaPlayer(playUrl)
    }

    fun initState() {
        _playTime.postValue(0)
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
        isPlaying = true
        _playState.value = PlayState.Play
        timerJob = viewModelScope.launch {
            while (isPlaying) {
                _playTime.postValue(audioPlayer.getCurrentPosition())
                delay(TIMER_DELAY)
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        isPlaying = false
        audioPlayer.pause()
        _playState.value = PlayState.Pause
    }

    override fun onCleared() {
        closePlayer()
    }

    companion object {
        const val TIMER_DELAY = 500L
    }
}