package com.example.playlistmaker.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ThemePreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        THEME_PREFERENCES, Context.MODE_PRIVATE
    )

    fun getSharedPreferences(): SharedPreferences {
        val sharedPreferences = this.sharedPreferences
        return sharedPreferences
    }

    fun setDarkThemePreferences(isEnabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(ENABLED_DARK_THEME, isEnabled)
        }
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(ENABLED_DARK_THEME, false)
    }

    fun clearThemePreferences() {
        sharedPreferences.edit { clear() }
    }

    companion object {
        const val THEME_PREFERENCES = "theme_preferences"
        const val ENABLED_DARK_THEME = "enabled_dark_theme"
    }
}