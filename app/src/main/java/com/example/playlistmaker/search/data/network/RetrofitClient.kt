package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ResponseResultDto
import com.example.playlistmaker.search.data.dto.TrackRequestDto
import kotlinx.coroutines.CancellationException
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
            val result = tracksApi.getTracksByQuery(trackRequestDto.query)
            ResponseResultDto.Success(data = result)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ResponseResultDto.Failure(e.message.toString())
        }
}