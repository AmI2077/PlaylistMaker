package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.interfaces.ResourceClient
import com.example.playlistmaker.sharing.domain.model.EmailData

class ResourceClientImpl(private val context: Context): ResourceClient {

    override fun getShareMessage() = context.getString(R.string.share)

    override fun getTermsUrl() = context.getString(R.string.agreementUrl)

    override fun getEmailData() = EmailData(
        emailAddress = context.getString(R.string.emailSendTo),
        themeMessage = context.getString(R.string.emailThemeMessage),
        message = context.getString(R.string.messageToDevelopers)
    )
}