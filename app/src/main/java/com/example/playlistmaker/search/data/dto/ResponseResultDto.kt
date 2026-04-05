package com.example.playlistmaker.search.data.dto

sealed interface ResponseResultDto {
    class Success(val data: TrackResponseDto?): ResponseResultDto
    object Failure: ResponseResultDto
}