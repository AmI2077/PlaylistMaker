package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApiService {

    @GET("/search?entity=song")
    suspend fun getTracksByQuery(
        @Query("term") query: String
    ) : Response<TrackResponseDto>
}