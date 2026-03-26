package com.example.playlistmaker.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ThemePreferencesImpl(context: Context): ThemePreferences {
    private val sharedPreferences = context.getSharedPreferences(
        THEME_PREFERENCES, Context.MODE_PRIVATE
    )

    override fun get(): SharedPreferences {
        val sharedPreferences = this.sharedPreferences
        return sharedPreferences
    }

    override fun set(isEnabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(ENABLED_DARK_THEME, isEnabled)
        }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(ENABLED_DARK_THEME, false)
    }

    companion object {
        const val THEME_PREFERENCES = "theme_preferences"
        const val ENABLED_DARK_THEME = "enabled_dark_theme"
    }
}