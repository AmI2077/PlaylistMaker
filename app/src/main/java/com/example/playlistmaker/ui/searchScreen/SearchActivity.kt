package com.example.playlistmaker.ui.searchScreen

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.ui.playerScreen.PlayerActivity
import com.example.playlistmaker.ui.searchScreen.tracksRecycler.TracksAdapter
import com.example.playlistmaker.ui.searchScreen.state.SearchHistoryState
import com.example.playlistmaker.ui.searchScreen.state.SearchScreenUiState
import com.example.playlistmaker.ui.searchScreen.state.SearchState
import kotlinx.coroutines.Runnable

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.createSearchViewModelFactory(applicationContext)
    }

    private val mainHandler by lazy {
        Handler(mainLooper)
    }

    private lateinit var mainView: ConstraintLayout
    private lateinit var editText: EditText
    private lateinit var clearSearchBtn: ImageView
    private lateinit var historyClearBtn: Button
    private lateinit var refreshBtn: Button
    private lateinit var searchRecycler: RecyclerView
    private lateinit var historySearchRecycler: RecyclerView
    private lateinit var historySearchView: FrameLayout
    private lateinit var emptyErrorView: ConstraintLayout
    private lateinit var networkErrorView: ConstraintLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var tracksAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        setViews()

        viewModel.initState()
        viewModel.userQuery.observe(this, setupEditTextObserver())
        viewModel.screenState.observe(this) { state ->
            render(state)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = 0
            val verticalPadding = 0
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
        setupAdapters()
    }

    private fun render(state: SearchScreenUiState) {
        hideViews()
        if (state.historyVisibility) {
            when (state.historyState) {
                SearchHistoryState.EmptyHistory -> historySearchView.visibility = View.GONE
                is SearchHistoryState.History -> {
                    tracksAdapter.submitList(state.historyState.searchedTracks)
                    historySearchView.visibility = View.VISIBLE
                }
            }
        }
        when (state.searchState) {
            SearchState.EmptyResult -> emptyErrorView.visibility = View.VISIBLE
            is SearchState.Error -> emptyErrorView.visibility = View.VISIBLE
            SearchState.Idle -> {}
            SearchState.Loading -> progressBar.visibility = View.VISIBLE
            SearchState.NetworkError -> networkErrorView.visibility = View.VISIBLE
            is SearchState.Success -> {
                tracksAdapter.submitList(state.searchState.tracks)
                searchRecycler.visibility = View.VISIBLE
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
            val searchRunnable = Runnable {

                if (!s.toString().isEmpty()) loadTracks(s.toString())
            }
            viewModel.setUserQuery(s.toString())
            mainHandler.removeCallbacks(searchRunnable)
            mainHandler.postDelayed(searchRunnable, SEARCH_DELAY)
        }
    }


    private fun loadTracks(query: String) {
        viewModel.getTracksByQuery(query)
    }

    private fun onTrackClick(track: Track) {
        viewModel.addTrackToHistory(track)
        if (clickDebounce()) {
            val intent = PlayerActivity.createIntent(applicationContext, track)
            startActivity(intent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (current) {
            isClickAllowed = false
            mainHandler.postDelayed({isClickAllowed = true}, TRACK_CLICK_DELAY)
        }
        return current
    }

    private fun onHistoryClearBtnClick() {
        viewModel.clearSearchHistory()
    }

    private fun onMainViewClick() {
        hideKeyboard()
        editText.clearFocus()
    }

    private fun onClearSearchBtnClick() {
        editText.setText("")
        editText.clearFocus()
        viewModel.onClearSearchBar()

        hideKeyboard()
    }

    private fun setClearBtnVisibility(s: String) {
        if (s.isNotEmpty()) {
            clearSearchBtn.visibility = View.VISIBLE
        } else {
            clearSearchBtn.visibility = View.GONE
        }
    }

    private fun setupAdapters() {
        tracksAdapter = TracksAdapter(
            onItemClick = { track ->
                onTrackClick(track)
            }
        )
        searchRecycler.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        searchRecycler.adapter = tracksAdapter

        historySearchRecycler.layoutManager = LinearLayoutManager(applicationContext)
        historySearchRecycler.adapter = tracksAdapter
    }

    private fun setupClickListeners() {
        mainView.setOnClickListener { onMainViewClick() }
        clearSearchBtn.setOnClickListener { onClearSearchBtnClick() }
        refreshBtn.setOnClickListener { viewModel.getTracksByQuery(viewModel.userQuery.value!!) }
        historyClearBtn.setOnClickListener { onHistoryClearBtnClick() }
    }

    private fun hideViews() {
        searchRecycler.visibility = View.GONE
        networkErrorView.visibility = View.GONE
        progressBar.visibility = View.GONE
        emptyErrorView.visibility = View.GONE
        historySearchView.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun setViews() {
        mainView = findViewById<ConstraintLayout>(R.id.main)
        editText = findViewById<EditText>(R.id.editText)
        clearSearchBtn = findViewById<ImageView>(R.id.clear_search_btn)
        historyClearBtn = findViewById<Button>(R.id.clear_history_btn)
        refreshBtn = findViewById<Button>(R.id.refresh_btn)
        searchRecycler = findViewById<RecyclerView>(R.id.tracks_recycler)
        emptyErrorView = findViewById<ConstraintLayout>(R.id.empty_result_view)
        networkErrorView = findViewById<ConstraintLayout>(R.id.network_error_view)
        historySearchRecycler = findViewById<RecyclerView>(R.id.history_search_recycler)
        historySearchView = findViewById<FrameLayout>(R.id.search_history_view)
        progressBar = findViewById<ProgressBar>(R.id.loading)
    }


    companion object {
        const val SEARCH_DELAY = 2000L
        const val TRACK_CLICK_DELAY = 1000L
        var isClickAllowed = true
    }
}