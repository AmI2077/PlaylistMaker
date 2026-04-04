package com.example.playlistmaker.search.data.extensions

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.TrackTimeUtil

fun TrackDto.toModel(): Track {
    return Track(
        trackId = trackId ?: "",
        trackName = trackName ?: "",
        artistName = artistName ?: "",
        trackTimeMillis = TrackTimeUtil.formatTrackTime(trackTimeMillis  ?: ""),
        artworkUrl100 = artworkUrl100 ?: "",
        collectionName = collectionName ?: "",
        releaseDate = releaseDate ?: "",
        country = country ?: "",
        primaryGenreName = primaryGenreName ?: "",
        previewUrl = previewUrl ?: ""
    )
}