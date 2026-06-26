package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.library.ui.model.PlaylistsUiState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private var _state = MutableLiveData<PlaylistsUiState>()
    val state: LiveData<PlaylistsUiState> = _state

    init {
        getPlaylists()
    }
    private fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect {
                _state.value = PlaylistsUiState(
                    playlists = it
                )
            }
        }
    }
}