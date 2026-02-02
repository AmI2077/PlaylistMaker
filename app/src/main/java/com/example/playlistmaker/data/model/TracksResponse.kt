package com.example.playlistmaker.data.model

data class TracksResponse(
    val resultCount: Int,
    val results: MutableList<Track>,
)
