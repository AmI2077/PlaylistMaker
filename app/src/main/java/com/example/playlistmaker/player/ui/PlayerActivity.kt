package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.DimensionsUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding

    private val playerViewModel: PlayerViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupClickListeners()
        setTrackInfo(getTrackInfo(intent))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playerViewModel.playState.observe(this) { state ->
            render(state)
        }
        playerViewModel.playTime.observe(this) { time ->
            setTrackTime(time)
        }

        lifecycle.addObserver(object: DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                playerViewModel.pause()
            }
        })
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
            .transform(RoundedCorners(DimensionsUtils.Companion.dpToPixel(8f, applicationContext)))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(view)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getTrackInfo(intent: Intent): Track? {
        return intent.getParcelableExtra(TRACK_INFO_KEY, Track::class.java)
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
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

    companion object {
        fun createIntent(context: Context, track: Track): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_INFO_KEY, track)
            return intent
        }

        const val TRACK_INFO_KEY = "track_info"
    }
}