package com.example.playlistmaker.search.data.dto

sealed interface ResponseResultDto {
    data class Success(val data: TrackResponseDto?): ResponseResultDto
    data class Failure(val message: String): ResponseResultDto
}