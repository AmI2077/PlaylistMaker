package com.example.playlistmaker.ui.playerScreen

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.ui.playerScreen.state.PlayState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayer: MediaPlayer
): ViewModel() {
    var playUrl = ""
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
                delay(500)
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
        fun createPlayerViewModelFactory(): ViewModelProvider.Factory {
            val mediaPlayer = MediaPlayer()
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PlayerViewModel(mediaPlayer) as T
                    } else {
                        throw IllegalArgumentException("non PlayerViewModel class")
                    }
                }
            }
        }
    }
}