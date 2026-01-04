package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.preferences.ThemePreferences
import androidx.core.content.edit

class App: Application() {
    lateinit var themePreferences: ThemePreferences
    var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()
        themePreferences = ThemePreferences(this)
        setTheme()
    }

    private fun setTheme() {
        if (themePreferences.getSharedPreferences().contains(ThemePreferences.ENABLED_DARK_THEME)) {
            if (themePreferences.isDarkTheme()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun switchTheme(enabled: Boolean) {
        isDarkTheme = enabled
        themePreferences.clearThemePreferences()
        themePreferences.setDarkThemePreferences(isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}