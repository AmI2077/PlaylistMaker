package com.example.playlistmaker.sharing.domain.interfaces

import com.example.playlistmaker.library.domain.model.Playlist

interface SharingInteractor {
    fun shareApp()
    fun openEmail()
    fun openTerms()
    fun sharePlaylist(playlist: Playlist)
}