package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.search.data.local.PrefsStorageClient
import com.example.playlistmaker.search.data.local.StorageClient
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.ResourceClientImpl
import com.example.playlistmaker.sharing.domain.interfaces.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interfaces.ResourceClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        RetrofitClient
    } bind NetworkClient::class

    single {
        PrefsStorageClient(
            sharedPreferences = androidContext().getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE),
            get()
        )
    } bind StorageClient::class

    single {
        ExternalNavigatorImpl(
            context = androidContext(),
            get()
        )
    } bind ExternalNavigator::class

    single {
        ResourceClientImpl(androidContext())
    } bind ResourceClient::class

    single {
        Gson()
    }
}

private const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
