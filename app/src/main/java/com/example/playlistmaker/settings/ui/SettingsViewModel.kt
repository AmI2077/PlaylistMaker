package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.example.playlistmaker.settings.domain.interfaces.ThemeInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeInteractor
) : ViewModel() {

    private var _themeState = MutableLiveData<ThemeState>(ThemeState.LightThemeState)
    val themeState: LiveData<ThemeState> get() = _themeState

    init {
        _themeState.value =
            if (themeInteractor.getTheme()) ThemeState.DarkThemeState else ThemeState.LightThemeState

    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OpenTerms -> openTerms()
            is Event.SendEmail -> sendEmail()
            is Event.ShareApp -> shareApp()
            is Event.SetDarkTheme -> setDarkTheme()
            is Event.SetLightTheme -> setLightTheme()
        }
    }

    private fun setLightTheme() {
        _themeState.value = ThemeState.LightThemeState
        themeInteractor.setLightTheme()
    }

    private fun setDarkTheme() {
        _themeState.value = ThemeState.DarkThemeState
        themeInteractor.setDarkTheme()
    }

    private fun openTerms() {
        sharingInteractor.openTerms()
    }

    private fun sendEmail() {
        sharingInteractor.openEmail()
    }

    private fun shareApp() {
        sharingInteractor.shareApp()
    }
}