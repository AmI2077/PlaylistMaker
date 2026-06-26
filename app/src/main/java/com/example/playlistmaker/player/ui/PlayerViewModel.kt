package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val TIMER_DELAY = 300L

class PlayerViewModel(
    private val audioPlayer: AudioPlayer,
    private val playerInteractor: PlayerInteractor,
    private val playlistInteractor: PlaylistsInteractor
): ViewModel() {

    private var timerJob: Job? = null

    private var trackId: String = ""

    private var _uiEvents = MutableSharedFlow<PlayerUiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    private var _state = MutableLiveData<PlayerUiState>()
    val state: LiveData<PlayerUiState> = _state

    fun onIntent(intent: PlayerIntent) {
        when(intent) {
            is PlayerIntent.LoadTrack -> {
                trackId = intent.id
                initState()
                prepareMediaPlayer(intent.url)
            }
            PlayerIntent.Pause -> {
                updatePlayerUiState(
                    isPlaying = false,
                    playTime = audioPlayer.getCurrentPosition(),
                )
                pause()
            }
            PlayerIntent.Play -> {
                start()
            }

            is PlayerIntent.FavBtnClick -> {
                onFavClick(intent.track)
            }

            is PlayerIntent.AddTrackToPlaylist -> {
                viewModelScope.launch {
                    if (trackInPlaylist(intent.track.trackId, intent.playlistId)) {
                        _uiEvents.emit(PlayerUiEvents.ShowTrackAlreadyExistsMessage)
                    } else {
                        addTrackToPlaylist(intent.track, intent.playlistId)
                        _uiEvents.emit(PlayerUiEvents.ShowTrackSuccessAddedMessage)
                    }
                }
            }
        }
    }

    fun initState() {
        timerJob?.cancel()
        viewModelScope.launch {
            val isLiked = playerInteractor.getTrack(trackId)
            val inPlaylist = playerInteractor.getPlaylist(trackId).isNotEmpty()
            updatePlayerUiState(
                isPlaying = false,
                playTime = 0,
                isLiked = isLiked,
                inPlaylist = inPlaylist
            )
        }
    }

    private fun start() {
        timerJob?.cancel()
        audioPlayer.play()

        updatePlayerUiState(isPlaying = true, playTime = audioPlayer.getCurrentPosition())

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(TIMER_DELAY)
                updatePlayerUiState(
                    isPlaying = true,
                    playTime = audioPlayer.getCurrentPosition(),
                )
            }
        }
    }

    private fun pause() {
        timerJob?.cancel()
        audioPlayer.pause()
    }

    private fun onFavClick(track: Track) {
        viewModelScope.launch {
            val isLiked = playerInteractor.getTrack(track.trackId)
            if (isLiked) {
                playerInteractor.deleteTrackFromFav(track.trackId)
            } else {
                playerInteractor.addTrackToFav(track)
            }
            val currentState = _state.value ?: PlayerUiState()
            _state.value = currentState.copy(
                isLiked = !isLiked
            )
        }
    }

    private suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        playlistInteractor.addTrackIntoPlaylist(playlistId, track)
        val currentState = _state.value ?: PlayerUiState()
        _state.value = currentState.copy(
            inPlaylist = true
        )
    }

    private suspend fun trackInPlaylist(trackId: String, playlistId: Int): Boolean {
        return playerInteractor.getPlaylist(trackId).contains(playlistId)
    }

    private fun updatePlayerUiState(
        isPlaying: Boolean,
        playTime: Int,
        isLiked: Boolean? = null,
        inPlaylist: Boolean? = null
    ) {
        val currentState = _state.value ?: PlayerUiState()
        _state.value = currentState.copy(
            isPlaying = isPlaying,
            playTime = playTime,
            isLiked = isLiked ?: currentState.isLiked,
            inPlaylist = inPlaylist ?: currentState.inPlaylist
        )
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

    override fun onCleared() {
        closePlayer()
    }
}
