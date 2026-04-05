package com.example.playlistmaker.di

import android.content.Context
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

