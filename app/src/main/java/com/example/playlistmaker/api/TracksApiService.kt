package com.example.playlistmaker.api

import com.example.playlistmaker.models.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApiService {

    @GET("/search?entity=song")
    fun searchTracksByQuery(
        @Query("term") query: String
    ) : Call<TracksResponse>
}