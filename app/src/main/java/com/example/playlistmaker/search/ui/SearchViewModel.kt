package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.interfaces.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.SearchResult
import com.example.playlistmaker.search.ui.state.SearchError
import com.example.playlistmaker.search.ui.state.SearchScreenIntent
import com.example.playlistmaker.search.ui.state.SearchScreenUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private var _userInput = MutableStateFlow<String>(SearchScreenUiState().userQuery)

    private var _screenState = MutableLiveData<SearchScreenUiState>()
    val screenState: LiveData<SearchScreenUiState> = _screenState

    init {
        initState()
        observeUserInput()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeUserInput() {
        viewModelScope.launch {
            _userInput
                .onEach { query ->
                    val currentState = _screenState.value ?: SearchScreenUiState()

                    if (query.isEmpty()) {
                        initState()
                    } else {
                        updateScreenState(currentState.copy(
                            loading = true,
                            error = null,
                            historyVisible = false,
                            userQuery = query
                        ))
                    }
                }
                .distinctUntilChanged()
                .debounce(SEARCH_DELAY)
                .flatMapLatest { query ->
                    if (query.isEmpty()) {
                        flowOf()
                    } else {
                        tracksInteractor.searchTracksByQuery(query)
                    }
                }
                .collect { result ->
                    handleSearchResult(result)
                }
        }
    }

    fun onIntent(intent: SearchScreenIntent) {
        when(intent) {
            SearchScreenIntent.ClearHistory -> {
                clearSearchHistory()
            }
            SearchScreenIntent.ClearSearch -> {
                clearSearch()
            }
            is SearchScreenIntent.Search -> {
                _userInput.value = intent.query
            }
            is SearchScreenIntent.TrackClick -> {
                addTrackToHistory(intent.track)
            }
            SearchScreenIntent.RefreshSearch -> {
                viewModelScope.launch {
                    tracksInteractor.searchTracksByQuery(_userInput.value)
                        .collect { handleSearchResult(it) }
                }
            }
        }
    }

    fun initState() {
        viewModelScope.launch {
            tracksInteractor.getSearchHistory().collect { history ->
                updateScreenState(
                    SearchScreenUiState(
                        loading = false,
                        userQuery = "",
                        searchedTracks = null,
                        error = null,
                        historyVisible = history.isNotEmpty(),
                        historyTracks = history
                    )
                )
            }
        }
    }

    private fun handleSearchResult(result: SearchResult) {
        val currentState = _screenState.value ?: SearchScreenUiState()

        val newState = when(result) {
            SearchResult.Empty -> currentState.copy(
                loading = false,
                error = SearchError.EmptyResult,
                searchedTracks = null,
                historyVisible = false
            )
            SearchResult.NetworkError -> currentState.copy(
                loading = false,
                error = SearchError.NetworkError,
                searchedTracks = null,
                historyVisible = false
            )
            is SearchResult.Success -> currentState.copy(
                loading = false,
                searchedTracks = result.tracks,
                error = null,
                historyVisible = false
            )
        }
        updateScreenState(newState)
    }

    private fun updateScreenState(newState: SearchScreenUiState) {
        _screenState.postValue(newState)
    }

    private fun clearSearchHistory() {
        viewModelScope.launch {
            tracksInteractor.clearSearchHistory()
            updateScreenState(SearchScreenUiState(
                historyVisible = false,
                historyTracks = null
            ))
        }
    }

    private fun clearSearch() {
        _userInput.value = ""

        val currentState = _screenState.value ?: SearchScreenUiState()
        val hasHistory = !currentState.historyTracks.isNullOrEmpty()

        updateScreenState(
            currentState.copy(
                loading = false,
                userQuery = "",
                searchedTracks = null,
                error = null,
                historyVisible = hasHistory
            )
        )
    }

    private fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            val newList = tracksInteractor.addTrackToHistory(track)
            val currentState = _screenState.value ?: SearchScreenUiState()

            updateScreenState(
                currentState.copy(
                    historyTracks = newList
                )
            )
        }
    }

    companion object {
        const val SEARCH_DELAY = 2000L
    }
}