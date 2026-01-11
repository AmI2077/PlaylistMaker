package com.example.playlistmaker.data.preferences

import com.example.playlistmaker.data.model.Track

interface TracksPreferences {
    fun read(): List<Track>
    fun write(tracks: List<Track>)
    fun clear()
}