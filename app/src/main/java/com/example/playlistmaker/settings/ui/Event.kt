package com.example.playlistmaker.settings.ui

sealed interface Event {

    object ShareApp: Event
    object SendEmail: Event
    object OpenTerms: Event
    object SetLightTheme: Event
    object SetDarkTheme: Event
}