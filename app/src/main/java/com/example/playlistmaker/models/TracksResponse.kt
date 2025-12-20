package com.example.playlistmaker.models

data class TracksResponse(
    val resultCount: Int,
    val results: MutableList<Track>,
)
