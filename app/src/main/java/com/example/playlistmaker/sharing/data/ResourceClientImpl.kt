package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.Playlist
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

    override fun getPlaylistTextData(playlist: Playlist): String {
        val builder = StringBuilder()

        builder.append(playlist.title).append("\n")

        if (!playlist.description.isNullOrBlank()) {
            builder.append(playlist.description).append("\n")
        }
        val tracksCount = context.resources.getQuantityString(
            R.plurals.tracksCountInPlaylist,
            playlist.countTracks,
            playlist.countTracks
        )
        builder.append(tracksCount).append("\n")

        playlist.tracks.forEachIndexed { index, track ->
            val number = index + 1
            builder.append("$number. ${track.artistName} - ${track.trackName} (${track.trackTimeMillis})")
            if (index < playlist.tracks.lastIndex) {
                builder.append("\n")
            }
        }
        return builder.toString()
    }
}