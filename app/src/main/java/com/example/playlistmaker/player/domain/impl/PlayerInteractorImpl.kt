package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {
    override suspend fun getTrack(id: String): Boolean {
        return playerRepository.getTrack(id) != null
    }

    override suspend fun getPlaylist(trackId: String): List<Int> {
        return playerRepository.getPlaylist(trackId)
    }

    override suspend fun addTrackToFav(track: Track) {
        playerRepository.addTrackToFav(track)
    }

    override suspend fun deleteTrackFromFav(id: String) {
        playerRepository.deleteTrackFromFav(id)
    }
}