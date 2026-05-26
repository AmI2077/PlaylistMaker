package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.DimensionsUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlin.getValue


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel: PlayerViewModel by viewModel()

    private val args: PlayerFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setTrackInfo(getTrackInfo())

        playerViewModel.playState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        playerViewModel.playTime.observe(viewLifecycleOwner) { time ->
            setTrackTime(time)
        }
    }

    override fun onStop() {
        super.onStop()
        playerViewModel.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTrackTime(time: Int) {
        binding.trackTimeNow.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    @SuppressLint("SetTextI18n")
    private fun render(state: PlayState) {
        when(state) {
            PlayState.Idle -> {
                binding.playBtn.visibility = View.VISIBLE
                binding.pauseBtn.visibility = View.GONE
            }
            PlayState.Pause -> {
                binding.playBtn.visibility = View.VISIBLE
                binding.pauseBtn.visibility = View.GONE
            }
            PlayState.Play -> {
                binding.playBtn.visibility = View.GONE
                binding.pauseBtn.visibility = View.VISIBLE
            }
        }
    }


    private fun setTrackInfo(track: Track?) {
        Log.d("track", track.toString())
        if (track != null) {
            playerViewModel.setUrl(track.previewUrl)
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
            .transform(RoundedCorners(DimensionsUtils.Companion.dpToPixel(8f, requireContext())))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(view)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getTrackInfo(): Track? {
        return args.track
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
    }

    private fun onPlayBtnClick() {
        playerViewModel.start()
    }

    private fun onPauseBtnClick() {
        playerViewModel.pause()
    }

}