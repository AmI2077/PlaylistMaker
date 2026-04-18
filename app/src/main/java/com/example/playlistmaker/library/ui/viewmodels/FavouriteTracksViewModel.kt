package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.ui.model.LibraryUiState

class FavouriteTracksViewModel(): ViewModel() {

    private var _state = MutableLiveData<LibraryUiState>(LibraryUiState.Empty)
    val state: LiveData<LibraryUiState> get() = _state

}