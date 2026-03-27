package com.example.playlistmaker.settings.ui

sealed interface ThemeState {
    object LightThemeState: ThemeState
    object DarkThemeState: ThemeState
}