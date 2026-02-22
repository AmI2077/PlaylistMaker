package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.ResponseResultDto
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TrackRequestDto
import com.example.playlistmaker.data.mapper.Mapper
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.interfaces.TracksRepository
import com.example.playlistmaker.domain.models.SearchResult
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackMapper: Mapper<TrackDto, Track>,
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
                            trackMapper.map(it)
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
