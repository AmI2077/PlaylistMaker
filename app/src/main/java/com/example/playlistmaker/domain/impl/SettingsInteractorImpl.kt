package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.sharedPrefs.ThemePreferencesImpl
import com.example.playlistmaker.domain.interfaces.SettingsInteractor

class SettingsInteractorImpl(
    private val themePreferencesImpl: ThemePreferencesImpl,
    override val themePrefsConst: String,
    override val enabledDarkConst: String
): SettingsInteractor {

    override fun getPrefs(): SharedPreferences {
        return themePreferencesImpl.get()
    }

    override fun setPrefs(isEnabled: Boolean) {
        themePreferencesImpl.set(isEnabled)
    }

    override fun clear() {
        themePreferencesImpl.clear()
    }

    override fun isDarkTheme(): Boolean {
        return themePreferencesImpl.isDarkTheme()
    }
}