package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.playlistdetails.PlaylistDetailsIntent
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {
    private var _state = MutableLiveData<Playlist>()
    val state: LiveData<Playlist> = _state


    fun onIntent(intent: PlaylistDetailsIntent) {
        when(intent) {
            is PlaylistDetailsIntent.LoadDetails -> getDetails(intent.playlistId)
            PlaylistDetailsIntent.OpenMenuBottomSheet -> Unit
            is PlaylistDetailsIntent.DeletePlaylist -> Unit
        }
    }

    private fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlistId)
        }
    }

    private fun getDetails(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistDetails(playlistId).collect {
                _state.value = it
            }
        }
    }
}