package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.api.TracksApiService
import com.example.playlistmaker.data.model.RequestResult
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.model.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class TracksRepositoryImpl(
    private val tracksApiService: TracksApiService,
): TrackRepository {
    override fun getTracksByQuery(
        query: String,
        callback: (RequestResult<List<Track>>) -> Unit
    ) {
        tracksApiService.getTracksByQuery(query).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse?>,
                response: Response<TracksResponse?>
            ) {
                callback(handleResponse(response))
            }

            override fun onFailure(
                call: Call<TracksResponse?>,
                t: Throwable
            ) {
                callback(RequestResult.Failure(t))
            }
        })
    }

    private fun handleResponse(response: Response<TracksResponse?>): RequestResult<List<Track>> {
        return if (response.isSuccessful) {
            RequestResult.Success(response.body()?.results.orEmpty())
        } else {
            RequestResult.Failure(HttpException(response))
        }
    }
}