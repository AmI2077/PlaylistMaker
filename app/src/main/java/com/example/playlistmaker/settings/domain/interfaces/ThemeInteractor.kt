package com.example.playlistmaker.settings.domain.interfaces

interface ThemeInteractor {

    fun setDarkTheme()
    fun setLightTheme()
    fun getTheme(): Boolean
}