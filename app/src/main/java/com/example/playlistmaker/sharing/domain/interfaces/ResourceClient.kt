package com.example.playlistmaker.sharing.domain.interfaces

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ResourceClient {

    fun getShareMessage(): String
    fun getTermsUrl(): String
    fun getEmailData(): EmailData
}