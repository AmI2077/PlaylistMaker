package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ResponseResultDto
import com.example.playlistmaker.data.dto.TrackRequestDto

interface NetworkClient {

    suspend fun requestTracks(trackRequestDto: TrackRequestDto): ResponseResultDto
}