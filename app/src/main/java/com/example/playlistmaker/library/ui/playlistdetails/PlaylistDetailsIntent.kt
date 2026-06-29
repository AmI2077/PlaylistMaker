package com.example.playlistmaker.library.ui.playlistdetails

import com.example.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistDetailsIntent {

    data class LoadDetails(val playlistId: Int): PlaylistDetailsIntent
    data object OpenMenuBottomSheet: PlaylistDetailsIntent
    data class SharePlaylist(val playlist: Playlist): PlaylistDetailsIntent
    data class DeletePlaylist(val playlistId: Int): PlaylistDetailsIntent
    data class DeleteTrack(val playlistId: Int, val trackId: String): PlaylistDetailsIntent
}