package com.example.playlistmaker.search.data.local

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T
    fun clearData()
}