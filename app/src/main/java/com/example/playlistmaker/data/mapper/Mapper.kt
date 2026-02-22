package com.example.playlistmaker.data.mapper

interface Mapper<From, To> {
    fun map(from: From): To
}