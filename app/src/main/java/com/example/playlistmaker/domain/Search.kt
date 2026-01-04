package com.example.playlistmaker.domain

import com.example.playlistmaker.data.api.RetrofitClient
import com.example.playlistmaker.ui.searchScreen.state.SearchState
import com.example.playlistmaker.data.repository.TracksRepository

class Search() {
    fun execute(
        query: String,
        onState: (SearchState) -> Unit,
    ) {
        TracksRepository(RetrofitClient.tracksApi) { result ->
            onState(result)
        }.search(query)
    }
}
