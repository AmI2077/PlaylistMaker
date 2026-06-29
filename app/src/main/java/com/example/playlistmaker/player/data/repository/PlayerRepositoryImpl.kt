package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.library.data.db.dao.PlaylistTrackDao
import com.example.playlistmaker.library.data.db.dao.FavouriteTrackDao
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.search.data.extensions.toFavEntity
import com.example.playlistmaker.search.data.extensions.toModel
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(
    private val favouriteTrackDao: FavouriteTrackDao,
    private val playlistTrackDao: PlaylistTrackDao
): PlayerRepository {
    override suspend fun getTrack(id: String): Track? =
        withContext(Dispatchers.IO) {
            favouriteTrackDao.getTrackFromFavById(id)?.toModel()
        }

    override suspend fun getPlaylist(trackId: String): List<Int> {
        return withContext(Dispatchers.IO) {
            playlistTrackDao.getPlaylistForTrack(trackId)
        }
    }

    override suspend fun addTrackToFav(track: Track) {
        favouriteTrackDao.insertTrackInFav(track.toFavEntity())
    }

    override suspend fun deleteTrackFromFav(id: String) {
        favouriteTrackDao.deleteTrackFromFav(id)
    }
}


