package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.api.FavTracksInteractor
import com.example.playlistmaker.library.domain.api.FavTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavTracksInteractorImpl(
   private val favTracksRepository: FavTracksRepository
): FavTracksInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return favTracksRepository.getTracks().map { it.asReversed() }
    }
}