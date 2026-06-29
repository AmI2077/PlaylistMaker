package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.api.PlaylistRepository
import com.example.playlistmaker.library.domain.api.PlaylistsInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylists()

    override fun getPlaylistDetails(playlistId: Int): Flow<Playlist> =
        playlistRepository.getPlaylistDetails(playlistId)

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
    }

    override suspend fun addTrackIntoPlaylist(
        playlistId: Int,
        track: Track
    ) {
        playlistRepository.addTrackIntoPlaylist(playlistId, track)
    }
}