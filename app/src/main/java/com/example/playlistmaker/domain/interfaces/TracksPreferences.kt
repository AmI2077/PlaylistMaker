package com.example.playlistmaker.domain.interfaces

import com.example.playlistmaker.domain.models.Track

interface TracksPreferences {
    fun read(): List<Track>
    fun write(tracks: List<Track>)
    fun clear()
}