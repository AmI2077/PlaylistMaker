package com.example.playlistmaker.data.preferences

import android.content.Context
import com.example.playlistmaker.data.model.Track
import com.google.gson.Gson
import androidx.core.content.edit

class SearchHistoryPreferences(context: Context) : TracksPreferences {
    private val sharedPreferences = context.getSharedPreferences(
        SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE
    )

    val jsonObj = Gson()

    override fun read(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)
        return if (json == null) {
            listOf()
        } else {
            jsonObj.fromJson(json, Array<Track>::class.java).toList()
        }
    }

    override fun write(tracks: List<Track>) {
        val json = jsonObj.toJson(tracks)
        sharedPreferences.edit {
            putString(SEARCH_HISTORY, json)
        }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }

    companion object {
        const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        const val SEARCH_HISTORY = "search_history"
    }
}