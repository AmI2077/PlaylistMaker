package com.example.playlistmaker.domain.interfaces

import android.content.SharedPreferences

interface SettingsInteractor {

    val themePrefsConst: String
    val enabledDarkConst: String

    fun getPrefs(): SharedPreferences
    fun setPrefs(isEnabled: Boolean)
    fun clear()
    fun isDarkTheme(): Boolean
}