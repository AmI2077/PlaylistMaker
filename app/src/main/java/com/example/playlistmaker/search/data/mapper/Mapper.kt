package com.example.playlistmaker.search.data.mapper

interface Mapper<From, To> {
    fun map(from: From): To
}