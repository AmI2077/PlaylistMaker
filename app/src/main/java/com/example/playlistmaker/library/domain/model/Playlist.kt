package com.example.playlistmaker.library.domain.model

import com.example.playlistmaker.search.domain.models.Track

data class Playlist(
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val artworkUri: String? = null,
    val createDate: String,
    val totalTracksTime: Int = 0,
    val countTracks: Int = 0,
    val tracks: List<Track> = emptyList()
)