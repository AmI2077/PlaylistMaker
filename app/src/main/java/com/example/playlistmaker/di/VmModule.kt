package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        PlayerViewModel(get())
    }
    factory {
        MediaPlayer()
    }
}