package com.example.playlistmaker.search.data.dto

data class TrackResponseDto(
    val resultCount: Int,
    val results: List<TrackDto>,
)