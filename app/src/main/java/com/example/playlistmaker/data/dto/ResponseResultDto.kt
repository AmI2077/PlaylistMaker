package com.example.playlistmaker.data.dto

sealed class ResponseResultDto {
    class Success(val data: TrackResponseDto?): ResponseResultDto()
    object Failure: ResponseResultDto()
}