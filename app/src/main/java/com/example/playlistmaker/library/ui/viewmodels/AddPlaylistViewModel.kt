package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch

class AddPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor): ViewModel() {

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
}