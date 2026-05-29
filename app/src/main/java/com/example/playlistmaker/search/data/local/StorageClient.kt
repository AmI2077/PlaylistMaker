package com.example.playlistmaker.search.data.local

interface StorageClient<T> {
    suspend fun storeData(data: T)
    suspend fun getData(): T
    suspend fun clearData()
}