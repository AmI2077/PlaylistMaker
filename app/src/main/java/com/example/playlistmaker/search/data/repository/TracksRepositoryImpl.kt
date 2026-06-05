package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.dto.ResponseResultDto
import com.example.playlistmaker.search.data.dto.TrackRequestDto
import com.example.playlistmaker.search.data.extensions.toModel
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.interfaces.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {

    override fun searchTracksByQuery(query: String): Flow<SearchResult> = flow {
        val response = networkClient.requestTracks(TrackRequestDto(query))

        when (response) {
            is ResponseResultDto.Success -> {
                if (response.data?.results?.isEmpty() == true) {
                   emit(SearchResult.Empty)
                } else {
                    emit(SearchResult.Success(
                        tracks = response.data?.results?.map {
                            it.toModel()
                        } ?: emptyList(),
                    ))
                }
            }
            is ResponseResultDto.Failure -> {
                emit(SearchResult.NetworkError)
            }
        }
    }
}
