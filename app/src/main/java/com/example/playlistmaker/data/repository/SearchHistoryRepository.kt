package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.models.Track

interface SearchHistoryRepository {
    fun get(): List<Track>
    fun save(tracks: List<Track>)
    fun clear()
}