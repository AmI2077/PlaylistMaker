package com.example.playlistmaker.data.preferences

import android.content.SharedPreferences

interface ThemePreferences {
    fun get(): SharedPreferences
    fun set(isEnabled: Boolean)
    fun clear()
}