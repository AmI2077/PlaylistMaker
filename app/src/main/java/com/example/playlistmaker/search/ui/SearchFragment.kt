package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.state.SearchError
import com.example.playlistmaker.search.ui.state.SearchScreenIntent
import com.example.playlistmaker.search.ui.state.SearchScreenUiState
import com.example.playlistmaker.search.ui.tracksRecycler.TracksAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var searchTracksAdapter: TracksAdapter
    private lateinit var historyTracksAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        setEditText()
        setupClickListeners()
        setupAdapters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: SearchScreenUiState) {
        val currentQuery = state.userQuery

        setClearBtnVisibility(currentQuery)

        if (binding.editText.text.toString() != currentQuery) {
            binding.editText.setText(currentQuery)
            binding.editText.setSelection(currentQuery.length)
        }

        hideViews()

        when {
            state.loading -> {
                binding.loading.visibility = View.VISIBLE
            }
            state.error != null -> {
                when(state.error) {
                    SearchError.EmptyResult -> binding.emptyResultView.visibility = View.VISIBLE
                    SearchError.NetworkError -> binding.networkErrorView.visibility = View.VISIBLE
                }
            }
            state.historyVisible -> {
                historyTracksAdapter.submitList(state.historyTracks.orEmpty())
                binding.searchHistoryView.visibility = View.VISIBLE
            }
            state.searchedTracks != null -> {
                searchTracksAdapter.submitList(state.searchedTracks)
                binding.tracksRecycler.visibility = View.VISIBLE
            }
        }
    }

    private fun setEditText() {
        binding.editText.requestFocus()
        binding.editText.doOnTextChanged { s, p1, p2, p3 ->
            viewModel.onIntent(SearchScreenIntent.Search(s.toString()))
        }
    }
    private fun onTrackClick(track: Track) {
        Log.d("TRACK_CLICK", isClickAllowed.toString())
        if (isClickAllowed) {
            isClickAllowed = false
            viewModel.onIntent(SearchScreenIntent.TrackClick(track))
            val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
            findNavController().navigate(action)
            lifecycleScope.launch {
                delay(TRACK_CLICK_DELAY)
                isClickAllowed = true
            }
        }
    }

    private fun onHistoryClearBtnClick() {
        viewModel.onIntent(SearchScreenIntent.ClearHistory)
    }

    private fun onMainViewClick() {
        hideKeyboard()
        binding.editText.clearFocus()
    }

    private fun onClearSearchBtnClick() {
        binding.editText.clearFocus()
        viewModel.onIntent(SearchScreenIntent.ClearSearch)
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
        searchTracksAdapter = TracksAdapter { track -> onTrackClick(track) }
        binding.tracksRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksRecycler.adapter = searchTracksAdapter

        historyTracksAdapter = TracksAdapter { track -> onTrackClick(track) }
        binding.historySearchRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historySearchRecycler.adapter = historyTracksAdapter
    }

    private fun setupClickListeners() {
        binding.main.setOnClickListener { onMainViewClick() }
        binding.clearSearchBtn.setOnClickListener { onClearSearchBtnClick() }
        binding.refreshBtn.setOnClickListener { viewModel.onIntent(SearchScreenIntent.RefreshSearch) }
        binding.clearHistoryBtn.setOnClickListener { onHistoryClearBtnClick() }
    }

    private fun hideViews() {
        binding.tracksRecycler.visibility = View.GONE
        binding.networkErrorView.visibility = View.GONE
        binding.loading.visibility = View.GONE
        binding.emptyResultView.visibility = View.GONE
        binding.searchHistoryView.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        var isClickAllowed = true
        const val TRACK_CLICK_DELAY = 1000L
    }
}