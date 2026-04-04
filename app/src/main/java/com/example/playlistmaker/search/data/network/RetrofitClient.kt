package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ResponseResultDto
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TrackRequestDto
import com.example.playlistmaker.search.data.dto.TrackResponseDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient : NetworkClient {

    private const val BASE_URL = "https://itunes.apple.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tracksApi: TracksApiService = retrofit.create(TracksApiService::class.java)

    override suspend fun requestTracks(trackRequestDto: TrackRequestDto): ResponseResultDto =
        try {
            val response = tracksApi.getTracksByQuery(trackRequestDto.query)
            ResponseResultDto.Success(
                data = TrackResponseDto(
                    response.body()?.resultCount ?: 0,
                    response.body()?.results ?: emptyList<TrackDto>()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseResultDto.Failure
        }
}