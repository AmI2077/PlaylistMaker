package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.playlistdetails.PlaylistDetailsIntent
import com.example.playlistmaker.sharing.domain.interfaces.SharingInteractor
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {
    private var _state = MutableLiveData<Playlist>()
    val state: LiveData<Playlist> = _state


    fun onIntent(intent: PlaylistDetailsIntent) {
        when(intent) {
            is PlaylistDetailsIntent.LoadDetails -> getDetails(intent.playlistId)
            PlaylistDetailsIntent.OpenMenuBottomSheet -> Unit
            is PlaylistDetailsIntent.DeletePlaylist -> {
                deletePlaylist(playlistId = intent.playlistId)
            }
            is PlaylistDetailsIntent.DeleteTrack -> {
                deleteTrackFromPlaylist(intent.trackId, intent.playlistId)
            }
            is PlaylistDetailsIntent.SharePlaylist -> {
                sharingInteractor.sharePlaylist(intent.playlist)
            }
        }
    }

    private fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrackFromPlaylist(playlistId, trackId)
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
                _state.value = it.copy(
                    tracks = it.tracks.asReversed()
                )
            }
        }
    }
}