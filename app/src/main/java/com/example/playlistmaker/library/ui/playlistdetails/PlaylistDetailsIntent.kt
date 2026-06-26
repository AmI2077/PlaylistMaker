package com.example.playlistmaker.library.ui.playlistdetails

sealed interface PlaylistDetailsIntent {

    data class LoadDetails(val playlistId: Int): PlaylistDetailsIntent
    object OpenMenuBottomSheet: PlaylistDetailsIntent
    data class DeletePlaylist(val playlistId: Int): PlaylistDetailsIntent
}