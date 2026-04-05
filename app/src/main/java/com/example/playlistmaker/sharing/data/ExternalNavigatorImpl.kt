package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.sharing.domain.interfaces.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interfaces.ResourceClient

class ExternalNavigatorImpl(
    private val context: Context,
    private val resourceClient: ResourceClient
): ExternalNavigator {

    override fun shareApp() {
        val message = resourceClient.getShareMessage()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(
            Intent.createChooser(
                shareIntent, null
            ).addActivityFlag()
        )
    }

    override fun openEmail() {
        val emailData = resourceClient.getEmailData()
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.themeMessage)
            putExtra(Intent.EXTRA_TEXT, emailData.message)
        }.addActivityFlag()
        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(emailIntent)
        } else {
            Log.d("sharing", "app not found")
        }
    }

    override fun openTerms() {
        val url = resourceClient.getTermsUrl()
        val termsIntent = Intent(Intent.ACTION_VIEW, url.toUri()).addActivityFlag()
        context.startActivity(termsIntent)
    }
}

private fun Intent.addActivityFlag(): Intent {
    return apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}