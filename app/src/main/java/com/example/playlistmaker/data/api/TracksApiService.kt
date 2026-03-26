package com.example.playlistmaker.data.api

import com.example.playlistmaker.data.model.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApiService {

    @GET("/search?entity=song")
    fun getTracksByQuery(
        @Query("term") query: String
    ) : Call<TracksResponse>
}