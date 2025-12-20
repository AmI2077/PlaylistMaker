package com.example.playlistmaker.api

import com.example.playlistmaker.models.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksRepository(
    private val tracksApiService: TracksApiService,
    private val callback: (SearchResult) -> Unit
) {
    fun search(query: String) {
        tracksApiService.searchTracksByQuery(query).enqueue(object : Callback<TracksResponse>{
            override fun onResponse(
                call: Call<TracksResponse?>,
                response: Response<TracksResponse?>
            ) {
                if (response.isSuccessful) {
                    if(response.body()?.results?.isNotEmpty() == true) {
                        val tracks = response.body()?.results!!
                        callback(SearchResult.Success(tracks))
                    } else {
                        callback(SearchResult.EmptyResult())
                    }
                } else {
                    callback(SearchResult.Error(response.errorBody()?.string().toString()))
                }
            }

            override fun onFailure(
                call: Call<TracksResponse?>,
                t: Throwable
            ) {
                t.printStackTrace()
                callback(SearchResult.NetworkError())
            }

        })
    }

}