package com.example.playlistmaker.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.ui.ThemeState

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        val isDarkTheme =
            getSharedPreferences(ThemeRepositoryImpl.THEME_PREFERENCES, MODE_PRIVATE)
                .getBoolean(ThemeRepositoryImpl.DARK_THEME, false)

        initTheme(isDarkTheme)
    }

    private fun initTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun switchTheme(themeState: ThemeState): Boolean = when (themeState) {
        ThemeState.DarkThemeState -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            true
        }
        ThemeState.LightThemeState -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            false
        }
    }
}