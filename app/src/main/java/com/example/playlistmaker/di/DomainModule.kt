package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.api.FavTracksInteractor
import com.example.playlistmaker.library.domain.impl.FavTracksInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.interfaces.TracksInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.settings.domain.interfaces.ThemeInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interfaces.SharingInteractor
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    single {
        FavTracksInteractorImpl(get())
    } bind FavTracksInteractor::class

    single {
        PlayerInteractorImpl(get())
    } bind PlayerInteractor::class

    single {
        TracksInteractorImpl(get(), get())
    } bind TracksInteractor::class

    single {
        ThemeInteractorImpl(get())
    } bind ThemeInteractor::class

    single {
        SharingInteractorImpl(get())
    } bind SharingInteractor::class
}