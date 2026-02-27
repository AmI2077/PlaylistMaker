package com.example.playlistmaker.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.sharedPrefs.ThemePreferencesImpl
import com.example.playlistmaker.di.ServiceCreator
import com.example.playlistmaker.domain.interfaces.SettingsInteractor

class App: Application() {
    lateinit var themePrefs: SettingsInteractor
    var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()
        themePrefs = ServiceCreator.getSettingsInteractor(this)
        setTheme()
    }

    private fun setTheme() {
        if (themePrefs.getPrefs().contains(ThemePreferencesImpl.Companion.ENABLED_DARK_THEME)) {
            if (themePrefs.isDarkTheme()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun switchTheme(enabled: Boolean) {
        isDarkTheme = enabled
        themePrefs.clear()
        themePrefs.setPrefs(isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}