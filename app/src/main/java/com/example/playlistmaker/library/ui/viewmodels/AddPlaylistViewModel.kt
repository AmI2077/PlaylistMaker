package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch

class AddPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor): ViewModel() {

    private var _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    fun addPlaylist(
        title: String,
        description: String? = null,
        imagePath: String? = null,
        nowYear: String
    ) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    title = title,
                    description = description,
                    artworkUri = imagePath,
                    createDate = nowYear,
                )
            )
        }
    }

    fun getPlaylistDetails(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistDetails(playlistId).collect {
                _playlist.value = it
            }
        }
    }

    fun updatePlaylist(
        id: Int,
        title: String,
        description: String? = null,
        imagePath: String? = null,
        nowYear: String
    ) {
        viewModelScope.launch {
            viewModelScope.launch {
                playlistsInteractor.updatePlaylist(
                    Playlist(
                        id = id,
                        title = title,
                        description = description,
                        artworkUri = imagePath,
                        createDate = nowYear,
                    )
                )
            }
        }
    }
}