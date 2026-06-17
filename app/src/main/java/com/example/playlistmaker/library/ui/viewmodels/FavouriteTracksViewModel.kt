package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.FavTracksInteractor
import com.example.playlistmaker.library.ui.model.LibraryUiState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favTracksInteractor: FavTracksInteractor
): ViewModel() {

    private var _state = MutableLiveData<LibraryUiState>(LibraryUiState())
    val state: LiveData<LibraryUiState> get() = _state

    init {
        getTracks()
    }

    private fun getTracks() {
        viewModelScope.launch {
            favTracksInteractor.getTracks().collect {
                updateUiState(it)
            }
        }
    }

    private fun updateUiState(
        tracks: List<Track>
    ) {
        val currentState = _state.value ?: LibraryUiState()

        _state.value = currentState.copy(
            tracks = tracks
        )
    }
}