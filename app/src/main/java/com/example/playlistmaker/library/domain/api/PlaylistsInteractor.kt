package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistDetails(playlistId: Int): Flow<Playlist>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrackIntoPlaylist(playlistId: Int, track: Track)
}