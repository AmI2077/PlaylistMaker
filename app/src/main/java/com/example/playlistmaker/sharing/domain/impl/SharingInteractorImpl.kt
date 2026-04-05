package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.interfaces.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interfaces.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
): SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareApp()
    }

    override fun openEmail() {
        externalNavigator.openEmail()
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }
}