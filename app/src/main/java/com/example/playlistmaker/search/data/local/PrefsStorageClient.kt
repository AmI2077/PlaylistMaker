package com.example.playlistmaker.search.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class PrefsStorageClient(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
): StorageClient<List<Track>> {

    override fun storeData(data: List<Track>) {
        val json = gson.toJson(data)
        sharedPreferences.edit {
            putString(SEARCH_HISTORY, json)
        }
    }

    override fun getData(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)
        return if (json == null) {
            emptyList()
        } else {
            gson.fromJson(json, Array<Track>::class.java).toList()
        }
    }

    override fun clearData() {
        sharedPreferences.edit {
            clear()
        }
    }

    companion object {
        const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        const val SEARCH_HISTORY = "search_history"
    }
}