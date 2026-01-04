package com.example.playlistmaker.ui.searchScreen

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.preferences.SearchHistoryPreferences
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.ui.searchScreen.searchHistoryRecycler.SearchHistoryAdapter
import com.example.playlistmaker.ui.searchScreen.searchRecycler.SearchAdapter
import com.example.playlistmaker.ui.searchScreen.state.SearchHistoryState
import com.example.playlistmaker.ui.searchScreen.state.SearchScreenUiState
import com.example.playlistmaker.ui.searchScreen.state.SearchState

class SearchActivity : AppCompatActivity() {
    private val searchHistoryPreferences by lazy {
        SearchHistoryPreferences(applicationContext)
    }

    private val searchHistoryRepository by lazy {
        SearchHistoryRepositoryImpl(searchHistoryPreferences)
    }

    private val searchHistory by lazy {
        SearchHistory(searchHistoryRepository)
    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.SearchViewModelFactory(searchHistory)
    }

    private lateinit var mainView: LinearLayout
    private lateinit var editText: EditText
    private lateinit var clearSearchBtn: ImageView
    private lateinit var historyClearBtn: Button
    private lateinit var refreshBtn: Button
    private lateinit var tracksRecycler: RecyclerView
    private lateinit var historySearchRecycler: RecyclerView
    private lateinit var historySearchView: LinearLayout
    private lateinit var emptyErrorView: LinearLayout
    private lateinit var networkErrorView: LinearLayout
    private lateinit var loadingText: TextView

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        setViews()

        viewModel.userQuery.observe(this, setupEditTextObserver())
        viewModel.screenState.observe(this) { state ->
            render(state)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.horizontalScreenPadding)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.verticalScreenPadding)
            v.setPadding(
                horizontalPadding,
                systemBars.top + verticalPadding,
                horizontalPadding,
                systemBars.bottom
            )
            insets
        }

        setEditText()
        setupClickListeners()
        setupSearchAdapter()
        setupHistoryAdapter()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveSearchHistory()
    }

    private fun render(state: SearchScreenUiState) {
        hideViews()
        if (state.historyVisibility) {
            when (state.historyState) {
                SearchHistoryState.EmptyHistory -> historySearchView.visibility = View.GONE
                is SearchHistoryState.History -> {
                    historyAdapter.submitList(state.historyState.searchedTracks)
                    historySearchView.visibility = View.VISIBLE
                }
            }
        }
        when (state.searchState) {
            SearchState.EmptyResult -> emptyErrorView.visibility = View.VISIBLE
            is SearchState.Error -> emptyErrorView.visibility = View.VISIBLE
            SearchState.Idle -> {}
            SearchState.Loading -> loadingText.visibility = View.VISIBLE
            SearchState.NetworkError -> networkErrorView.visibility = View.VISIBLE
            is SearchState.Success -> {
                searchAdapter.submitList(state.searchState.tracks)
                tracksRecycler.visibility = View.VISIBLE
            }
        }
    }

    private fun setupEditTextObserver(): Observer<String> {
        return Observer<String> { query ->
            setClearBtnVisibility(query)
            viewModel.onQueryChanged(query)
        }
    }

    private fun setEditText() {
        editText.requestFocus()
        editText.setText(viewModel.userQuery.value)
        editText.doOnTextChanged { s, p1, p2, p3 ->
            viewModel.setUserQuery(s.toString())
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editText.clearFocus()
                loadTracks(editText.text.toString())
                clearSearchBtn.visibility = View.VISIBLE
                true
            }
            false
        }
    }

    private fun loadTracks(query: String) {
        viewModel.getSearchResult(query)
    }

    private fun onTrackClick(track: Track) {
        viewModel.addTrackToHistory(track)
    }

    private fun onHistoryClearBtnClick() {
        viewModel.clearSearchHistory()
    }

    private fun onMainViewClick() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        editText.clearFocus()
    }

    private fun onClearSearchBtnClick() {
        editText.setText("")
        editText.clearFocus()
        viewModel.onClearSearchBar()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun setClearBtnVisibility(s: String) {
        if (s.isNotEmpty()) {
            clearSearchBtn.visibility = View.VISIBLE
        } else {
            clearSearchBtn.visibility = View.GONE
        }
    }

    fun setupHistoryAdapter() {
        historyAdapter = SearchHistoryAdapter()
        historySearchRecycler.layoutManager = LinearLayoutManager(this)
        historySearchRecycler.adapter = historyAdapter
    }

    private fun setupSearchAdapter() {
        searchAdapter = SearchAdapter(
            onItemClick = { track ->
                onTrackClick(track)
            }
        )
        tracksRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecycler.adapter = searchAdapter
    }

    private fun setupClickListeners() {
        mainView.setOnClickListener { onMainViewClick() }
        clearSearchBtn.setOnClickListener { onClearSearchBtnClick() }
        refreshBtn.setOnClickListener { viewModel.getSearchResult(viewModel.userQuery.value!!) }
        historyClearBtn.setOnClickListener { onHistoryClearBtnClick() }
    }

    private fun hideViews() {
        tracksRecycler.visibility = View.GONE
        networkErrorView.visibility = View.GONE
        loadingText.visibility = View.GONE
        emptyErrorView.visibility = View.GONE
        historySearchView.visibility = View.GONE
    }

    private fun setViews() {
        mainView = findViewById<LinearLayout>(R.id.main)
        editText = findViewById<EditText>(R.id.editText)
        clearSearchBtn = findViewById<ImageView>(R.id.clear_search_btn)
        historyClearBtn = findViewById<Button>(R.id.clear_history_btn)
        refreshBtn = findViewById<Button>(R.id.refresh_btn)
        tracksRecycler = findViewById<RecyclerView>(R.id.tracks_recycler)
        emptyErrorView = findViewById<LinearLayout>(R.id.empty_result_view)
        networkErrorView = findViewById<LinearLayout>(R.id.network_error_view)
        historySearchRecycler = findViewById<RecyclerView>(R.id.history_search_recycler)
        historySearchView = findViewById<LinearLayout>(R.id.search_history_view)
        loadingText = findViewById<TextView>(R.id.loading)
    }
}