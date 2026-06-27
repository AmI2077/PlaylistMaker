package com.example.playlistmaker.library.ui.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.LibraryFragmentDirections
import com.example.playlistmaker.library.ui.playlistsrecycler.PlaylistsAdapter
import com.example.playlistmaker.library.ui.model.PlaylistsUiState
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistsAdapter: PlaylistsAdapter

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        setupClickListeners()
        setupAdapter()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        return binding.root
    }

    private fun render(state: PlaylistsUiState) {
        if (!state.playlists.isEmpty()) {
            playlistsAdapter.submitList(state.playlists)
            binding.emptyErrorView.isVisible = false
            binding.playlistsRecycler.isVisible = true
        } else {
            binding.playlistsRecycler.isVisible = false
            binding.emptyErrorView.isVisible = true
        }
    }

    private fun setupClickListeners() {
        binding.newPlaylistBtn.setOnClickListener {
            onNewPlaylistBtnClick()
        }
    }

    private fun setupAdapter() {
        playlistsAdapter = PlaylistsAdapter {
            onPlaylistClick(it)
        }
        binding.playlistsRecycler.adapter = playlistsAdapter
        binding.playlistsRecycler.layoutManager = GridLayoutManager(requireContext(), 2,
            GridLayoutManager.VERTICAL, false)
    }

    private fun onPlaylistClick(playlistId: Int) {
        val action = LibraryFragmentDirections.actionLibraryFragmentToPlaylistDetailsFragment(playlistId)
        findNavController().navigate(action)
    }

    private fun onNewPlaylistBtnClick() {
        val action = LibraryFragmentDirections.actionLibraryFragmentToAddPlaylistFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = PlaylistsFragment()
    }
}