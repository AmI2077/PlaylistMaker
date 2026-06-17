package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.library.ui.LibraryFragmentDirections
import com.example.playlistmaker.library.ui.model.LibraryUiState
import com.example.playlistmaker.library.ui.viewmodels.FavouriteTracksViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.tracksRecycler.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!

    private var favTracksAdapter: TracksAdapter? = null

    private val viewModel: FavouriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        setupAdapter()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: LibraryUiState) {
        if (state.tracks.isEmpty()) {
            binding.emptyErrorView.visibility = View.VISIBLE
        } else {
            binding.emptyErrorView.visibility = View.GONE
            favTracksAdapter?.submitList(state.tracks)
        }
    }

    private fun setupAdapter() {
        favTracksAdapter = TracksAdapter(onItemClick = { onTrackClick(it) })

        binding.favouriteTracksRecycler.adapter = favTracksAdapter
        binding.favouriteTracksRecycler.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun onTrackClick(track: Track) {
        val action = LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    companion object {

        fun newInstance(): Fragment = FavouriteTracksFragment()
    }
}