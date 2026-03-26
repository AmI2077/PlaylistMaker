package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ResponseResultDto
import com.example.playlistmaker.search.data.dto.TrackRequestDto

interface NetworkClient {

    suspend fun requestTracks(trackRequestDto: TrackRequestDto): ResponseResultDto
}