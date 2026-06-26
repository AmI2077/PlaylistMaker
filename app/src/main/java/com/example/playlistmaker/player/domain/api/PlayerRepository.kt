package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface PlayerRepository {

    suspend fun getTrack(id: String): Track?

    suspend fun addTrackToFav(track: Track)

    suspend fun deleteTrackFromFav(id: String)
}