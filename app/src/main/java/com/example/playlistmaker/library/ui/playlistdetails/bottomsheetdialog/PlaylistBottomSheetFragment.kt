package com.example.playlistmaker.library.ui.playlistdetails.bottomsheetdialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBottomSheetBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.model.PlaylistsUiState
import com.example.playlistmaker.library.ui.playlistdetails.PlaylistDetailsIntent
import com.example.playlistmaker.library.ui.viewmodels.PlaylistDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPlaylistBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val navArgs: PlaylistBottomSheetFragmentArgs by navArgs()

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    private var playlist: Playlist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBottomSheetBinding.inflate(inflater, container, false)

        viewModel.onIntent(PlaylistDetailsIntent.LoadDetails(playlistId = navArgs.playlistId))
        viewModel.state.observe(viewLifecycleOwner) {
            playlist = it
            render(it)
        }

        setupClickListeners()

        return binding.root
    }

    private fun render(state: Playlist) {
        binding.playlistTitle.text = state.title
        setImage(state.artworkUri)
        binding.tracksCount.text = requireContext().resources.getQuantityString(
            R.plurals.tracksCountInPlaylist,
            state.countTracks,
            state.countTracks
        )
    }

    private fun setImage(imagePath: String?) {
        Glide
            .with(requireContext())
            .load(imagePath)
            .transform(CenterCrop())
            .placeholder(R.drawable.ic_playlist_placeholder_160)
            .into(binding.playlistArtwork)
    }

    private fun setupClickListeners() {
        binding.deleteBtn.setOnClickListener {
            onDeletePlaylist(navArgs.playlistId)
        }
        binding.editBtn.setOnClickListener {
            val action = PlaylistBottomSheetFragmentDirections
                .actionPlaylistBottomSheetFragmentToAddPlaylistFragment(playlist!!.id)
            findNavController().navigate(action)
        }
        binding.shareBtn.setOnClickListener {
            if (playlist?.tracks?.isNotEmpty() == true) {
                viewModel.onIntent(PlaylistDetailsIntent.SharePlaylist(playlist!!))
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.shareTracksInPlaylist),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onDeletePlaylist(playlistId: Int) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.deletePlaylist)
            .setMessage(R.string.deletePlaylistMessage)
            .setNegativeButton(R.string.permissionNegativeBtn) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.permissionPositiveBtn) { dialog, _ ->
                dialog.dismiss()
                viewModel.onIntent(PlaylistDetailsIntent.DeletePlaylist(playlistId))
                findNavController().popBackStack(
                    R.id.libraryFragment, false
                )
            }
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}