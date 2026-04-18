package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.model.LibraryUiState
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

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
        when(state) {
            LibraryUiState.Content -> {

            }
            LibraryUiState.Empty -> binding.emptyErrorView.visibility = View.VISIBLE
            LibraryUiState.Error -> {

            }
            LibraryUiState.Loading -> {

            }
        }
    }


    companion object {

        fun newInstance() = PlaylistsFragment()
    }
}