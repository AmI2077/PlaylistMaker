package com.example.playlistmaker.domain.interfaces

import android.content.SharedPreferences

interface ThemePreferences {
    fun get(): SharedPreferences
    fun set(isEnabled: Boolean)
    fun clear()
}