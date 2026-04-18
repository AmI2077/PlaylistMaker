package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.settings.domain.interfaces.ThemeRepository

class ThemeRepositoryImpl(
    private val sharedPreferences: SharedPreferences
): ThemeRepository {

    override fun setDarkTheme() {
        sharedPreferences.edit {
            putBoolean(DARK_THEME, true)
        }
    }

    override fun setLightTheme() {
        sharedPreferences.edit {
            putBoolean(DARK_THEME, false)
        }
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }

    companion object {
        const val DARK_THEME = "dark_theme"
    }
}

const val THEME_PREFERENCES = "theme_preferences"