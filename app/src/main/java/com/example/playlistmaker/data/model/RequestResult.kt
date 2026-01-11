package com.example.playlistmaker.data.model

sealed class RequestResult<out T> {
    data class Success<T>(val data: T): RequestResult<T>()
    data class Failure(val throwable: Throwable): RequestResult<Nothing>()
}