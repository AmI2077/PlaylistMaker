package com.example.playlistmaker.ui.searchScreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.api.RetrofitClient
import com.example.playlistmaker.data.model.RequestResult
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.data.preferences.SearchHistoryPreferences
import com.example.playlistmaker.data.repository.SearchHistoryRepository
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.ui.searchScreen.state.SearchHistoryState
import com.example.playlistmaker.ui.searchScreen.state.SearchScreenUiState
import com.example.playlistmaker.ui.searchScreen.state.SearchState
import java.net.UnknownHostException

class SearchViewModel(
    private val tracksRepositoryImpl: TracksRepositoryImpl,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    private val _userQuery = MutableLiveData<String>("")
    val userQuery: LiveData<String> = _userQuery

    private var _screenState = MutableLiveData<SearchScreenUiState>()
    val screenState: LiveData<SearchScreenUiState> = _screenState

    fun initState() {
        if (_userQuery.value != "" && _userQuery.value != null) {
            getTracksByQuery(_userQuery.value!!.toString())
        } else {
            val history = searchHistoryRepository.get()

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
    }

    fun getTracksByQuery(query: String) {
        val current = _screenState.value ?: return

        _screenState.value = current.copy(
            searchState = SearchState.Loading,
            historyVisibility = false,
        )

        tracksRepositoryImpl.getTracksByQuery(query) { state ->
            refreshSearchState(state)
        }
    }

    private fun refreshSearchState(result: RequestResult<List<Track>>) {
        val current = _screenState.value ?: return

        when(result) {
            is RequestResult.Success<List<Track>> -> {
                if (!result.data.isEmpty()) {
                    _screenState.value = current.copy(
                        searchState = SearchState.Success(result.data)
                    )
                } else {
                    _screenState.value = current.copy(
                        searchState = SearchState.EmptyResult
                    )
                }
            }
            is RequestResult.Failure -> {
                when (result.throwable) {
                    is UnknownHostException -> {
                        _screenState.value = current.copy(
                            searchState = SearchState.NetworkError
                        )
                    }
                    else -> {
                        _screenState.value = current.copy(
                            searchState = SearchState.EmptyResult
                        )
                    }
                }
            }
        }
    }

    fun clearSearchHistory() {
        searchHistoryRepository.clear()
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

    fun addTrackToHistory(track: Track) {
        val currentHistory = searchHistoryRepository.get().toMutableList()
        val current = _screenState.value ?: return

        val iterator = currentHistory.iterator()
        var index: Int = -1
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.trackId == track.trackId) {
                index = currentHistory.indexOf(item)
                iterator.remove()
                break
            }
        }
        if (currentHistory.size >= 10) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }
        currentHistory.add(0, track)
        searchHistoryRepository.save(currentHistory)

        _screenState.value = current.copy(
            historyState = SearchHistoryState.History(currentHistory),
        )
    }

    fun setUserQuery(query: String) {
        _userQuery.value = query
    }

    companion object {
        fun createSearchViewModelFactory(context: Context): ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                val tracksRepositoryImpl = TracksRepositoryImpl(RetrofitClient.tracksApi)
                val searchHistoryRepository = SearchHistoryRepositoryImpl(SearchHistoryPreferences(context))

                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return SearchViewModel(tracksRepositoryImpl, searchHistoryRepository) as T
                    }
                    throw IllegalArgumentException("non searchViewModel")
                }
            }
        }
    }
}


