package com.example.playlistmaker.sharing.domain.interfaces

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.sharing.domain.model.EmailData

interface ResourceClient {

    fun getShareMessage(): String
    fun getTermsUrl(): String
    fun getEmailData(): EmailData
    fun getPlaylistTextData(playlist: Playlist): String
}