package com.example.playlistmaker.settings.domain.interfaces

interface ThemeRepository {
    fun setDarkTheme()
    fun setLightTheme()
    fun getTheme(): Boolean
}