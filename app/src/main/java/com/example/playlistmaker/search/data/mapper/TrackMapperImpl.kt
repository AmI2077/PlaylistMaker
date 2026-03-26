package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.TrackTimeUtil

object TrackMapperImpl: Mapper<TrackDto, Track> {
    override fun map(from: TrackDto): Track {
        return Track(
            trackId = from.trackId ?: "",
            trackName = from.trackName ?: "",
            artistName = from.artistName ?: "",
            trackTimeMillis = TrackTimeUtil.formatTrackTime(from.trackTimeMillis  ?: ""),
            artworkUrl100 = from.artworkUrl100 ?: "",
            collectionName = from.collectionName ?: "",
            releaseDate = from.releaseDate ?: "",
            country = from.country ?: "",
            primaryGenreName = from.primaryGenreName ?: "",
            previewUrl = from.previewUrl ?: ""
        )
    }
}