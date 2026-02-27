package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.mapper.TrackMapperImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.data.sharedPrefs.SearchHistoryPreferencesImpl
import com.example.playlistmaker.data.sharedPrefs.ThemePreferencesImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.interfaces.SearchHistoryRepository
import com.example.playlistmaker.domain.interfaces.SettingsInteractor
import com.example.playlistmaker.domain.interfaces.TracksRepository
import com.example.playlistmaker.presentation.viewModels.PlayerViewModel
import com.example.playlistmaker.presentation.viewModels.SearchViewModel

object ServiceCreator {

    private val networkClient = RetrofitClient
    private val trackMapper = TrackMapperImpl

    fun getSettingsInteractor(context: Context): SettingsInteractor {
        val themePrefs = ThemePreferencesImpl(context)
        return SettingsInteractorImpl(themePrefs, ThemePreferencesImpl.THEME_PREFERENCES,
            ThemePreferencesImpl.ENABLED_DARK_THEME)
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(networkClient, trackMapper)
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(SearchHistoryPreferencesImpl(context))
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
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