package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.interfaces.ThemeInteractor
import com.example.playlistmaker.settings.domain.interfaces.ThemeRepository

class ThemeInteractorImpl(
    private val themeRepository: ThemeRepository
): ThemeInteractor {
    override fun setDarkTheme() {
        themeRepository.setDarkTheme()
    }

    override fun setLightTheme() {
        themeRepository.setLightTheme()
    }

    override fun getTheme(): Boolean {
        return themeRepository.getTheme()
    }
}