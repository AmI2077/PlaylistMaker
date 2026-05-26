package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.library.ui.model.LibraryUiState
import com.example.playlistmaker.library.ui.viewmodels.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)

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

        fun newInstance(): Fragment = FavouriteTracksFragment()
    }
}