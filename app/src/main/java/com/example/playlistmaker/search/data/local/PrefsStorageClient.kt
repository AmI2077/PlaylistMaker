package com.example.playlistmaker.search.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class PrefsStorageClient(context: Context): StorageClient<List<Track>> {

    private val sharedPrefs = context.getSharedPreferences(
        SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE
    )

    private val gson = Gson()

    override fun storeData(data: List<Track>) {
        val json = gson.toJson(data)
        sharedPrefs.edit {
            putString(SEARCH_HISTORY, json)
        }
    }

    override fun getData(): List<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null)
        return if (json == null) {
            emptyList()
        } else {
            gson.fromJson(json, Array<Track>::class.java).toList()
        }
    }

    override fun clearData() {
        sharedPrefs.edit {
            clear()
        }
    }

    companion object {
        const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        const val SEARCH_HISTORY = "search_history"
    }
}