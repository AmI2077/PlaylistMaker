package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class ExternalNavigatorImpl(
    private val context: Context,
    private val resourceClient: ResourceClient): ExternalNavigator {

    override fun shareApp() {
        val message = resourceClient.getShareMessage()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(
            Intent.createChooser(
                shareIntent, null
            )
        )
    }

    override fun openEmail() {
        val emailData = resourceClient.getEmailData()
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:${emailData.emailAddress}".toUri()
            putExtra(Intent.EXTRA_SUBJECT, emailData.themeMessage)
            putExtra(Intent.EXTRA_TEXT, emailData.message)
        }
        context.startActivity(emailIntent)
    }

    override fun openTerms() {
        val url = resourceClient.getTermsUrl()
        val termsIntent = Intent(Intent.ACTION_VIEW, url.toUri())

        context.startActivity(termsIntent)
    }
}