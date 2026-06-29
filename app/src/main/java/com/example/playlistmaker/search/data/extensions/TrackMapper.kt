package com.example.playlistmaker.search.data.extensions

import com.example.playlistmaker.library.data.db.entities.FavouriteTracksEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackDataEntity
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

fun FavouriteTracksEntity.toModel(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        country = country,
        primaryGenreName = primaryGenreName,
        previewUrl = previewUrl
    )
}

fun PlaylistTrackDataEntity.toModel(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        country = country,
        primaryGenreName = primaryGenreName,
        previewUrl = previewUrl
    )
}

fun Track.toFavEntity(): FavouriteTracksEntity {
    return FavouriteTracksEntity(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        country = country,
        primaryGenreName = primaryGenreName,
        previewUrl = previewUrl
    )
}

fun Track.toPlaylistEntity(): PlaylistTrackDataEntity {
    return PlaylistTrackDataEntity(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        country = country,
        primaryGenreName = primaryGenreName,
        previewUrl = previewUrl
    )
}