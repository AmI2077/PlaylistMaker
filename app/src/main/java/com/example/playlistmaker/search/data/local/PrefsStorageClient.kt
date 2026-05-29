package com.example.playlistmaker.search.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PrefsStorageClient(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val dispatcher: CoroutineDispatcher
): StorageClient<List<Track>> {

    override suspend fun storeData(data: List<Track>) =
        withContext(dispatcher) {
            val json = gson.toJson(data)
            sharedPreferences.edit {
                putString(SEARCH_HISTORY, json)
            }
        }

    override suspend fun getData(): List<Track> =
        withContext(dispatcher) {
            val json = sharedPreferences.getString(SEARCH_HISTORY, null)
            if (json == null) {
                emptyList()
            } else {
                gson.fromJson(json, Array<Track>::class.java).toList()
            }
        }

    override suspend fun clearData() =
        withContext(dispatcher) {
            sharedPreferences.edit {
                clear()
            }
        }

    companion object {
        const val SEARCH_HISTORY = "search_history"
    }
}