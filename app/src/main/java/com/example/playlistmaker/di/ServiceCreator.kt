package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.interfaces.SearchHistoryRepository
import com.example.playlistmaker.settings.domain.interfaces.ThemeInteractor
import com.example.playlistmaker.search.domain.interfaces.TracksRepository
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.data.local.PrefsStorageClient
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.ResourceClientImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.google.gson.Gson

object ServiceCreator {

    private val networkClient = RetrofitClient

    fun getThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(
            ThemeRepositoryImpl(context)
        )
    }

    fun getSharingInteractor(context: Context): SharingInteractor {
        val resourceClient = ResourceClientImpl(context)
        return SharingInteractorImpl(ExternalNavigatorImpl(context, resourceClient))
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(networkClient)
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient(
                sharedPreferences = context.getSharedPreferences(
                    PrefsStorageClient.SEARCH_HISTORY_PREFERENCES,
                    Context.MODE_PRIVATE
                ),
                gson = Gson()
            )
        )
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    fun createSettingsViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(
                        getSharingInteractor(context),
                        getThemeInteractor(context)
                    ) as T
                } else {
                    throw IllegalArgumentException("non SettingsViewModel class")
                }
            }
        }
    }

    fun createPlayerViewModelFactory(): ViewModelProvider.Factory {
        return object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PlayerViewModel(getMediaPlayer()) as T
                } else {
                    throw IllegalArgumentException("non PlayerViewModel class")
                }
            }
        }
    }

    fun createSearchViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object: ViewModelProvider.Factory {
            val tracksInteractor = TracksInteractorImpl(
                getTracksRepository(),
                getSearchHistoryRepository(context),
            )

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SearchViewModel(tracksInteractor) as T
                }
                throw IllegalArgumentException("non searchViewModel")
            }
        }
    }
}