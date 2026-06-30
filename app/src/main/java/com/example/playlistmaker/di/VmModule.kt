package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.viewmodels.AddPlaylistViewModel
import com.example.playlistmaker.library.ui.viewmodels.FavouriteTracksViewModel
import com.example.playlistmaker.library.ui.viewmodels.PlaylistDetailsViewModel
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel {
        AddPlaylistViewModel(get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        PlaylistDetailsViewModel(get(), get())
    }

    viewModel {
        FavouriteTracksViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        PlayerViewModel(get(), get(), get())
    }
}