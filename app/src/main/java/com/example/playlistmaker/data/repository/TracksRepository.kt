package com.example.playlistmaker.data.repository

import com.example.playlistmaker.ui.searchScreen.state.SearchState
import com.example.playlistmaker.data.api.TracksApiService
import com.example.playlistmaker.data.models.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksRepository(
    private val tracksApiService: TracksApiService,
    private val callback: (SearchState) -> Unit
) {
     fun search(query: String) {
        tracksApiService.searchTracksByQuery(query).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse?>,
                response: Response<TracksResponse?>
            ) {
                if (response.isSuccessful) {
                    if(response.body()?.results?.isNotEmpty() == true) {
                        val tracks = response.body()?.results!!
                        callback(SearchState.Success(tracks))
                    } else {
                        callback(SearchState.EmptyResult)
                    }
                } else {
                    callback(SearchState.Error(response.errorBody()?.string().toString()))
                }
            }

            override fun onFailure(
                call: Call<TracksResponse?>,
                t: Throwable
            ) {
                t.printStackTrace()
                callback(SearchState.NetworkError)
            }
        })
    }
}