package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.di.ServiceCreator
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.SearchHistoryState
import com.example.playlistmaker.search.domain.models.SearchScreenUiState
import com.example.playlistmaker.search.domain.models.SearchState
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.ui.tracksRecycler.TracksAdapter
import kotlinx.coroutines.Runnable

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels {
        ServiceCreator.createSearchViewModelFactory(applicationContext)
    }

    private val mainHandler by lazy(mode = LazyThreadSafetyMode.NONE) {
        Handler(mainLooper)
    }

    private var searchRunnable: Runnable? = null

    private lateinit var tracksAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

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
                SearchHistoryState.EmptyHistory -> binding.searchHistoryView.visibility = View.GONE
                is SearchHistoryState.History -> {
                    tracksAdapter.submitList(state.historyState.searchedTracks)
                    binding.searchHistoryView.visibility = View.VISIBLE
                }
            }
        }
        when (state.searchState) {
            SearchState.EmptyResult ->  binding.emptyResultView.visibility = View.VISIBLE
            is SearchState.Error ->  binding.emptyResultView.visibility = View.VISIBLE
            SearchState.Idle -> {}
            SearchState.Loading -> binding.loading.visibility = View.VISIBLE
            SearchState.NetworkError -> binding.networkErrorView.visibility = View.VISIBLE
            is SearchState.Success -> {
                tracksAdapter.submitList(state.searchState.tracks)
                binding.tracksRecycler.visibility = View.VISIBLE
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
        binding.editText.requestFocus()
        binding.editText.setText(viewModel.userQuery.value)
        binding.editText.doOnTextChanged { s, p1, p2, p3 ->
            searchRunnable?.let { mainHandler.removeCallbacks(it) }
            searchRunnable = getSearchRunnable(s.toString())
            viewModel.setUserQuery(s.toString())
            searchRunnable?.let { mainHandler.postDelayed(it, SEARCH_DELAY)}
        }
    }

    private fun getSearchRunnable(s: String): Runnable {
        return kotlinx.coroutines.Runnable {
            if (!s.toString().isEmpty()) loadTracks(s.toString())
        }
    }


    private fun loadTracks(query: String) {
        viewModel.getTracksByQuery(query)
    }

    private fun onTrackClick(track: Track) {
        viewModel.addTrackToHistory(track)
        if (clickDebounce()) {
            val intent = PlayerActivity.Companion.createIntent(applicationContext, track)
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
        binding.editText.clearFocus()
    }

    private fun onClearSearchBtnClick() {
        binding.editText.setText("")
        binding.editText.clearFocus()
        viewModel.onClearSearchBar()

        hideKeyboard()
    }

    private fun setClearBtnVisibility(s: String) {
        if (s.isNotEmpty()) {
            binding.clearSearchBtn.visibility = View.VISIBLE
        } else {
            binding.clearSearchBtn.visibility = View.GONE
        }
    }

    private fun setupAdapters() {
        tracksAdapter = TracksAdapter(
            onItemClick = { track ->
                onTrackClick(track)
            }
        )
        binding.tracksRecycler.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.tracksRecycler.adapter = tracksAdapter

        binding.historySearchRecycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.historySearchRecycler.adapter = tracksAdapter
    }

    private fun setupClickListeners() {
        binding.main.setOnClickListener { onMainViewClick() }
        binding.clearSearchBtn.setOnClickListener { onClearSearchBtnClick() }
        binding.refreshBtn.setOnClickListener { viewModel.getTracksByQuery(viewModel.userQuery.value!!) }
        binding.clearHistoryBtn.setOnClickListener { onHistoryClearBtnClick() }
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun hideViews() {
        binding.tracksRecycler.visibility = View.GONE
        binding.networkErrorView.visibility = View.GONE
        binding.loading.visibility = View.GONE
        binding.emptyResultView.visibility = View.GONE
        binding.searchHistoryView.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        const val SEARCH_DELAY = 2000L
        const val TRACK_CLICK_DELAY = 1000L
        var isClickAllowed = true
    }
}