package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.addtoplaylist.AddTrackBottomSheetFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.DimensionsUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlin.getValue


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel()

    private val navArgs: PlayerFragmentArgs by navArgs()

    private var playlistTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setTrackInfo(navArgs.track)
        getFragmentResult()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvents.collect { event ->
                when(event) {
                    PlayerUiEvents.ShowTrackAlreadyExistsMessage -> {
                        showSnackBar(
                            view = binding.root,
                            message = getString(R.string.trackAlreadyExistsMessage, playlistTitle)
                        )
                    }
                    PlayerUiEvents.ShowTrackSuccessAddedMessage -> {
                        showSnackBar(
                            view = binding.root,
                            message = getString(R.string.trackSuccessAddedMessage, playlistTitle)
                        )
                    }
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onIntent(PlayerIntent.Pause)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFragmentResult() {
        setFragmentResultListener(AddTrackBottomSheetFragment.FRAGMENT_RESULT_KEY) { _, bundle ->
            playlistTitle = bundle.getString(AddTrackBottomSheetFragment.PLAYLIST_TITLE_KEY)
            val playlistId = bundle.getInt(AddTrackBottomSheetFragment.PLAYLIST_ID_KEY)
            viewModel.onIntent(PlayerIntent.AddTrackToPlaylist(navArgs.track, playlistId))
        }
    }

    private fun setTrackTime(time: Int) {
        binding.trackTimeNow.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    @SuppressLint("SetTextI18n")
    private fun render(state: PlayerUiState) {
        setTrackTime(state.playTime)

        if (state.isLiked) {
            binding.addToFavBtn.setImageResource(R.drawable.ic_fav_24)
        } else {
            binding.addToFavBtn.setImageResource(R.drawable.ic_add_to_fav_btn_51)
        }

        if (state.inPlaylist) {
            binding.addToPlaylistBtn.setImageResource(R.drawable.ic_in_playlist_51)
        } else {
            binding.addToPlaylistBtn.setImageResource(R.drawable.ic_add_to_playlist_btn_51)
        }

        if (state.isPlaying) {
            binding.playBtn.visibility = View.GONE
            binding.pauseBtn.visibility = View.VISIBLE
        } else {
            binding.playBtn.visibility = View.VISIBLE
            binding.pauseBtn.visibility = View.GONE
        }
    }


    private fun setTrackInfo(track: Track?) {
        if (track != null) {
            viewModel.onIntent(PlayerIntent.LoadTrack(url = track.previewUrl, id = track.trackId))
            setImage(track.artworkUrl100, binding.trackArtwork)
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackTime.text = track.trackTimeMillis
            binding.collectionName.text = track.collectionName
            binding.releaseDate.text = Instant.parse(track.releaseDate)
                .atZone(ZoneId.of("UTC"))
                .year.toString()
            binding.primaryGenreName.text = track.primaryGenreName
            binding.country.text = track.country
        }
    }

    private fun setImage(uri: String, view: ImageView) {
        Glide.with(this)
            .load(uri.replaceAfterLast("/", "512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(DimensionsUtils.dpToPixel(8f, requireContext())))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(view)
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playBtn.setOnClickListener {
            onPlayBtnClick()
        }
        binding.pauseBtn.setOnClickListener {
            onPauseBtnClick()
        }
        binding.addToFavBtn.setOnClickListener {
            onFavBtnClick(navArgs.track)
        }
        binding.addToPlaylistBtn.setOnClickListener {
            onAddToPlaylistClick(navArgs.track)
        }
    }

    private fun showSnackBar(message: String, view: View) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

        val customSnackbar = layoutInflater.inflate(R.layout.custom_snackbar, null)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val layout = snackbar.view as ViewGroup

        val text = customSnackbar.findViewById<TextView>(R.id.snackbar_message)
        text.text = message

        layout.setPadding(0, 0, 0, 0)
        layout.addView(customSnackbar)
        snackbar.show()
    }


    private fun onPlayBtnClick() {
        viewModel.onIntent(PlayerIntent.Play)
    }

    private fun onPauseBtnClick() {
        viewModel.onIntent(PlayerIntent.Pause)
    }

    private fun onFavBtnClick(track: Track) {
        viewModel.onIntent(PlayerIntent.FavBtnClick(track))
    }

    private fun onAddToPlaylistClick(track: Track) {
        val action = PlayerFragmentDirections.actionPlayerFragmentToAddTrackBottomSheetFragment(
            track
        )
        findNavController().navigate(action)
    }
}