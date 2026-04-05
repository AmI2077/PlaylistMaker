package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.interfaces.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.SearchHistoryState
import com.example.playlistmaker.search.domain.models.SearchScreenUiState
import com.example.playlistmaker.search.domain.models.SearchState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {
    private val _userQuery = MutableLiveData<String>("")
    val userQuery: LiveData<String> = _userQuery

    private var _screenState = MutableLiveData<SearchScreenUiState>()
    val screenState: LiveData<SearchScreenUiState> = _screenState

    fun initState() {
        viewModelScope.launch {
            if (_userQuery.value != "" && _userQuery.value != null) {
                getTracksByQuery(_userQuery.value!!.toString())
            } else {
                val state = tracksInteractor.getSearchHistory()
                _screenState.value = SearchScreenUiState(
                    searchState = SearchState.Idle,
                    historyState = state,
                    historyVisibility = checkHistoryState(state)
                )
            }
        }
    }

    fun getTracksByQuery(query: String) {
        val current = _screenState.value ?: return

        viewModelScope.launch {
            _screenState.value = current.copy(
                searchState = SearchState.Loading,
                historyVisibility = false,
            )

            val state = tracksInteractor.searchTracksByQuery(query)
            _screenState.value = current.copy(
                searchState = state,
                historyVisibility = false
            )
        }
    }

    fun clearSearchHistory() {
        val current = _screenState.value ?: return

        viewModelScope.launch {
            val state = tracksInteractor.clearSearchHistory()

            _screenState.value = current.copy(
                searchState = SearchState.Idle,
                historyState = state,
                historyVisibility = checkHistoryState(state),
            )
        }
    }

    fun onQueryChanged(query: String) {
        if (query.isEmpty()) {
            val current = _screenState.value ?: return

            _screenState.value = current.copy(
                searchState = SearchState.Idle,
                historyVisibility = true
            )
        } else {
            val current = _screenState.value ?: return

            _screenState.value = current.copy(
                historyVisibility = false
            )
        }
    }

    fun onClearSearchBar() {
        val current = _screenState.value ?: return

        _screenState.value = current.copy(
            searchState = SearchState.Idle,
            historyVisibility = checkHistoryState(current.historyState),
        )
    }

    fun addTrackToHistory(track: Track) {
        val current = _screenState.value ?: return

        viewModelScope.launch {
            val state = tracksInteractor.addTrackToHistory(track)
            _screenState.value = current.copy(
                historyState = state,
                historyVisibility = current.searchState is SearchState.Idle,
            )
        }
    }

    fun setUserQuery(query: String) {
        _userQuery.value = query
    }

    private fun checkHistoryState(state: SearchHistoryState): Boolean {
        return when (state) {
            SearchHistoryState.EmptyHistory -> false
            is SearchHistoryState.History -> true
        }
    }
}