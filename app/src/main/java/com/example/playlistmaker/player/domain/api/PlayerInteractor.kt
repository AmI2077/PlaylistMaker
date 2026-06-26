package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface PlayerInteractor {

    suspend fun getTrack(id: String): Boolean

    suspend fun addTrackToFav(track: Track)

    suspend fun deleteTrackFromFav(id: String)
}