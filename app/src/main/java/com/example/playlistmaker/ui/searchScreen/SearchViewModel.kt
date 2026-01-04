package com.example.playlistmaker.ui.searchScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.domain.Search
import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.ui.searchScreen.state.SearchHistoryState
import com.example.playlistmaker.ui.searchScreen.state.SearchScreenUiState
import com.example.playlistmaker.ui.searchScreen.state.SearchState

class SearchViewModel(
    private val searchHistory: SearchHistory
) : ViewModel() {
    private val _userQuery = MutableLiveData<String>("")
    val userQuery: LiveData<String> = _userQuery

    private var _screenState = MutableLiveData<SearchScreenUiState>()
    val screenState: LiveData<SearchScreenUiState> = _screenState

    init {
        val history = searchHistory.getSearchHistory()

        if (!history.isEmpty()) {
            _screenState.value = SearchScreenUiState(
                searchState = SearchState.Idle,
                historyState = SearchHistoryState.History(history),
                historyVisibility = true
            )
        } else {
            _screenState.value = SearchScreenUiState(
                searchState = SearchState.Idle,
                historyState = SearchHistoryState.EmptyHistory,
                historyVisibility = false
            )
        }
    }

    fun getSearchResult(query: String) {
        val current = _screenState.value ?: return

        _screenState.value = current.copy(
            searchState = SearchState.Loading,
            historyVisibility = false,
        )

        Search().execute(query) { state ->
            refreshSearchState(state)
        }
    }

    private fun refreshSearchState(state: SearchState) {
        val current = _screenState.value ?: return

        _screenState.value = current.copy(
            searchState = state,
            historyVisibility = false,
        )
    }

    fun clearSearchHistory() {
        searchHistory.clearSearchHistory()
        val current = _screenState.value ?: return

        _screenState.value = current.copy(
            searchState = SearchState.Idle,
            historyState = SearchHistoryState.EmptyHistory,
            historyVisibility = false,
        )
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
            historyVisibility = true,
        )
    }

    fun saveSearchHistory() {
        searchHistory.saveSearchHistory(searchHistory.getSearchHistory())
    }

    fun addTrackToHistory(track: Track) {
        val newHistory = searchHistory.addTrackToHistory(
            track, searchHistory.getSearchHistory().toMutableList()
        )

        searchHistory.saveSearchHistory(newHistory)

        val current = _screenState.value ?: return

        _screenState.value = current.copy(
            historyState = SearchHistoryState.History(newHistory),
        )
    }

    fun setUserQuery(query: String) {
        _userQuery.value = query
    }

    class SearchViewModelFactory(
        private val searchHistory: SearchHistory
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(searchHistory) as T
            }
            throw IllegalArgumentException("non searchViewModel")
        }
    }
}


