package com.example.playlistmaker.library.ui.playlistdetails

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.viewmodels.PlaylistDetailsViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.tracksRecycler.TracksAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val navArgs: PlaylistDetailsFragmentArgs by navArgs()

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    private var playlist: Playlist? = null

    private lateinit var tracksAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        viewModel.onIntent(PlaylistDetailsIntent.LoadDetails(navArgs.playlistId))
        viewModel.state.observe(viewLifecycleOwner) {
            playlist = it
            render(it)
        }

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
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
        binding.menuBtn.setOnClickListener {
            val action = PlaylistDetailsFragmentDirections.
            actionPlaylistDetailsFragmentToPlaylistBottomSheetFragment(playlist!!.id)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: Playlist) {
        binding.playlistTitle.text = state.title
        binding.createDate.text = state.createDate
        if (!state.description.isNullOrEmpty()) {
            binding.description.text = state.description
            binding.description.visibility = View.VISIBLE
        } else {
            binding.description.visibility = View.GONE
        }
        binding.countTracks.text = requireContext().resources.getQuantityString(
            R.plurals.tracksCountInPlaylist,
            state.countTracks,
            state.countTracks
        )
        binding.totalTracksTime.text = requireContext().resources.getQuantityString(
            R.plurals.tracksTotalTimeInPlaylist,
            state.totalTracksTime,
            state.totalTracksTime
        )

        setImage(state.artworkUri)

        showBottomSheet(state.tracks)
    }

    private fun setImage(imagePath: String?) {
        Glide
            .with(requireContext())
            .load(imagePath)
            .transform(CenterCrop())
            .placeholder(R.drawable.ic_playlist_placeholder_160)
            .into(binding.artWork)
    }

    private fun showBottomSheet(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            setupAdapter(tracks)
            val bottomSheet = binding.tracksBottomSheet
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showDeleteTrackDialog(track: Track) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.deleteTrackMessage)
            .setNegativeButton(R.string.permissionNegativeBtn) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.permissionPositiveBtn) { dialog, _ ->
                dialog.dismiss()
                viewModel.onIntent(PlaylistDetailsIntent.DeleteTrack(
                    navArgs.playlistId, track.trackId
                ))
            }.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
    }

    private fun setupAdapter(tracks: List<Track>) {
        tracksAdapter = TracksAdapter(
            onItemClick = {
                val action = PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToPlayerFragment(it)
                findNavController().navigate(action)
            },
            onLongItemClick = {
                showDeleteTrackDialog(it)
            }
        )
        binding.tracksRecycler.adapter = tracksAdapter
        binding.tracksRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        tracksAdapter.submitList(tracks)
    }
}