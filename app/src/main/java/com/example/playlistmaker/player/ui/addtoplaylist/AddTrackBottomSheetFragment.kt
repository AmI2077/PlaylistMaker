package com.example.playlistmaker.player.ui.addtoplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddTrackBottomSheetBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTrackBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddTrackBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()

    private lateinit var playlistsAdapter: PlaylistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTrackBottomSheetBinding.inflate(inflater, container, false)

        viewModel.state.observe(viewLifecycleOwner) {
            setupAdapter(it.playlists)
        }

        binding.newPlaylistBtn.setOnClickListener {
            onNewPlaylistBtn()
        }

        return binding.root
    }

    private fun onNewPlaylistBtn() {
        val action = AddTrackBottomSheetFragmentDirections.actionAddTrackBottomSheetFragmentToAddPlaylistFragment()
        findNavController().navigate(action)
    }

    private fun setupAdapter(playlists: List<Playlist>) {
        playlistsAdapter = PlaylistsAdapter { playlist ->
            setFragmentResult(
                FRAGMENT_RESULT_KEY,
                bundleOf(
                    PLAYLIST_TITLE_KEY to playlist.title,
                    PLAYLIST_ID_KEY to playlist.id
                )
            )
            findNavController().popBackStack(
                R.id.playerFragment,
                false
            )
        }
        playlistsAdapter.submitList(playlists)
        binding.playlistsRecycler.adapter = playlistsAdapter
        binding.playlistsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAGMENT_RESULT_KEY = "trackAddedInPlaylist"
        const val PLAYLIST_TITLE_KEY = "playlistTitle"
        const val PLAYLIST_ID_KEY = "playlistId"
    }
}