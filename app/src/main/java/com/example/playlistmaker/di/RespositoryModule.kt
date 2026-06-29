package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.library.data.repository.FavTracksRepositoryImpl
import com.example.playlistmaker.library.data.repository.PlaylistsRepositoryImpl
import com.example.playlistmaker.library.domain.api.FavTracksRepository
import com.example.playlistmaker.library.domain.api.PlaylistRepository
import com.example.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.interfaces.SearchHistoryRepository
import com.example.playlistmaker.search.domain.interfaces.TracksRepository
import com.example.playlistmaker.settings.data.THEME_PREFERENCES
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.interfaces.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single {
        PlaylistsRepositoryImpl(get(), get())
    } bind PlaylistRepository::class

    single {
        FavTracksRepositoryImpl(get())
    } bind FavTracksRepository::class

    single {
        PlayerRepositoryImpl(get(), get())
    } bind PlayerRepository::class

    single {
        TracksRepositoryImpl(get())
    } bind TracksRepository::class

    single {
        SearchHistoryRepositoryImpl(get())
    } bind SearchHistoryRepository::class

    single {
        ThemeRepositoryImpl(
            sharedPreferences = androidContext().getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        )
    } bind ThemeRepository::class
}

