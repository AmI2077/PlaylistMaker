package com.example.playlistmaker.library.ui.playlistdetails.bottomsheetdialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBottomSheetBinding
import com.example.playlistmaker.library.ui.playlistdetails.PlaylistDetailsIntent
import com.example.playlistmaker.library.ui.viewmodels.PlaylistDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistBottomSheetFragment : Fragment() {
    private var _binding: FragmentPlaylistBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val navArgs: PlaylistBottomSheetFragmentArgs by navArgs()

    private val viewModel: PlaylistDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBottomSheetBinding.inflate(inflater, container, false)

        setupClickListeners()

        return binding.root
    }


    private fun setupClickListeners() {
        binding.deleteBtn.setOnClickListener {
            onDeletePlaylist(navArgs.playlistId)
        }
    }

    private fun onDeletePlaylist(playlistId: Int) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.deletePlaylist)
            .setMessage(R.string.deletePlaylistMessage)
            .setNegativeButton(R.string.permissionPositiveBtn) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.permissionPositiveBtn) { dialog, _ ->
                viewModel.onIntent(PlaylistDetailsIntent.DeletePlaylist(playlistId))
                dialog.dismiss()
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