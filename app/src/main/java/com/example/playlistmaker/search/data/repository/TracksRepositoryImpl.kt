package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.dto.ResponseResultDto
import com.example.playlistmaker.search.data.dto.TrackRequestDto
import com.example.playlistmaker.search.data.extensions.toModel
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.interfaces.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResult

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {

    override suspend fun searchTracksByQuery(
        query: String
    ): SearchResult {
        val response = networkClient.requestTracks(TrackRequestDto(query))

        return when (response) {
            is ResponseResultDto.Success -> {
                if (response.data?.results?.isEmpty() == true) {
                    SearchResult.Empty
                } else {
                    SearchResult.Success(
                        tracks = response.data?.results?.map {
                            it.toModel()
                        } ?: emptyList(),
                    )
                }
            }
            is ResponseResultDto.Failure -> {
                SearchResult.NetworkError
            }
        }
    }
}
