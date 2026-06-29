package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.PlaylistTrackDao
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackCrossRef
import com.example.playlistmaker.library.domain.api.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.data.extensions.toModel
import com.example.playlistmaker.search.data.extensions.toPlaylistEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistsRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao
) : PlaylistRepository {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().combine(playlistTrackDao.getAllRelations()) { playlists, relations ->
            val playlistsMap = relations.groupBy { it.playlistId }

            playlists.map { playlist ->
                val tracks = playlistsMap[playlist.id] ?: emptyList()
                Playlist(
                    id = playlist.id,
                    title = playlist.title,
                    description = playlist.description,
                    artworkUri = playlist.artWorkUri,
                    createDate = playlist.createDate,
                    countTracks = tracks.size
                )
            }
        }
    }

    override fun getPlaylistDetails(playlistId: Int): Flow<Playlist> {
        return playlistDao.getPlaylistDetails(playlistId).combine(getTrackFromPlaylist(playlistId)) { details, tracks ->
            Playlist(
                id = details.id,
                title = details.title,
                description = details.description,
                artworkUri = details.artWorkUri,
                createDate = details.createDate,
                totalTracksTime = getTracksTime(tracks) / 60,
                countTracks = tracks.size,
                tracks = tracks
            )
        }
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylist(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: String) {
        playlistTrackDao.deleteTrackFromPlaylist(
            PlaylistTrackCrossRef(
                trackId = trackId,
                playlistId = playlistId
            )
        )
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistDao.insertPlaylist(playlist.toEntity())
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlist.toEntity())
    }

    override suspend fun addTrackIntoPlaylist(playlistId: Int, track: Track) {
        withContext(Dispatchers.IO) {
            playlistTrackDao.insertTrackData(track.toPlaylistEntity())
            playlistTrackDao.insertTrackIntoPlaylist(
                PlaylistTrackCrossRef(
                    playlistId = playlistId,
                    trackId = track.trackId
                )
            )
        }
    }

    private fun getTracksTime(tracks: List<Track>): Int {
        return tracks.sumOf {
            it.trackTimeMillis.toSeconds()
        }
    }

    private fun getTrackFromPlaylist(playlistId: Int): Flow<List<Track>> {
        return playlistTrackDao.getTracksFromPlaylist(playlistId).map { tracks ->
            tracks.map {
                it.toModel()
            }
        }
    }
}

fun Playlist.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id,
        title = title,
        description = description,
        createDate = createDate,
        artWorkUri = artworkUri,
    )
}

fun String.toSeconds(): Int {
    val parts = this.split(":")
    if (parts.size < 2) return 0

    val minutes = parts[0].toIntOrNull() ?: 0
    val seconds = parts[1].toIntOrNull() ?: 0

    return (minutes * 60) + seconds
}

