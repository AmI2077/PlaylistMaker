package com.example.playlistmaker.data.preferences

import android.content.Context
import com.example.playlistmaker.data.models.Track
import com.google.gson.Gson
import androidx.core.content.edit

class SearchHistoryPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE
    )

    fun read(): Array<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)
        return if (json == null) {
            arrayOf()
        } else {
            Gson().fromJson(json, Array<Track>::class.java)
        }
    }

    fun write(tracks: Array<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit {
            putString(SEARCH_HISTORY, json)
        }
    }

    fun clear() {
        sharedPreferences.edit { clear() }
    }

    companion object {
        const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        const val SEARCH_HISTORY = "search_history"
    }
}