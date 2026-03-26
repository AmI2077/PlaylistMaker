package com.example.playlistmaker.settings.data

import android.content.Context
import androidx.core.content.edit
import com.example.playlistmaker.settings.domain.interfaces.ThemeRepository

class ThemeRepositoryImpl(
    context: Context
): ThemeRepository {

    private val sharedPreferences = context.getSharedPreferences(
        THEME_PREFERENCES, Context.MODE_PRIVATE
    )

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
        const val THEME_PREFERENCES = "theme_preferences"
        const val DARK_THEME = "dark_theme"
    }
}