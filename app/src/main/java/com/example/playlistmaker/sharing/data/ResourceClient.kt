package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ResourceClient {

    fun getShareMessage(): String
    fun getTermsUrl(): String
    fun getEmailData(): EmailData
}