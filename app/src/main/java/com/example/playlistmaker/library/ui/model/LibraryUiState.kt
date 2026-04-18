package com.example.playlistmaker.library.ui.model

sealed interface LibraryUiState {

    object Content: LibraryUiState
    object Loading: LibraryUiState
    object Empty: LibraryUiState
    object Error: LibraryUiState
}