package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.preferences.ThemePreferencesImpl

class App: Application() {
    lateinit var themePreferencesImpl: ThemePreferencesImpl
    var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()
        themePreferencesImpl = ThemePreferencesImpl(this)
        setTheme()
    }

    private fun setTheme() {
        if (themePreferencesImpl.get().contains(ThemePreferencesImpl.ENABLED_DARK_THEME)) {
            if (themePreferencesImpl.isDarkTheme()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun switchTheme(enabled: Boolean) {
        isDarkTheme = enabled
        themePreferencesImpl.clear()
        themePreferencesImpl.set(isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}