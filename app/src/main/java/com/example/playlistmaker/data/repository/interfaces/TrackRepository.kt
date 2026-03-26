package com.example.playlistmaker.data.repository.interfaces

import com.example.playlistmaker.data.model.RequestResult
import com.example.playlistmaker.data.model.Track

interface TrackRepository {
    fun getTracksByQuery(query: String, callback: (RequestResult<List<Track>>) -> Unit)
}