package com.example.playlistmaker.ui.playerScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.di.ServiceCreator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.viewModels.PlayerViewModel
import com.example.playlistmaker.presentation.states.PlayState
import com.example.playlistmaker.utils.DimensionsUtils
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlin.getValue

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels {
        ServiceCreator.createPlayerViewModelFactory()
    }

    lateinit var backBtn: ImageButton
    lateinit var trackArtWork: ImageView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var addToPlaylistBtn: ImageButton
    lateinit var playTrackBtn: ImageButton
    lateinit var pauseTrackBtn: ImageButton
    lateinit var addToFavBtn: ImageButton
    lateinit var trackTime: TextView
    lateinit var collectionName: TextView
    lateinit var releaseDate: TextView
    lateinit var primaryGenreName: TextView
    lateinit var country: TextView
    private lateinit var trackTimeNow: TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)

        setViews()
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
        trackTimeNow.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    @SuppressLint("SetTextI18n")
    private fun render(state: PlayState) {
        when(state) {
            PlayState.Idle -> {
                playTrackBtn.visibility = View.VISIBLE
                pauseTrackBtn.visibility = View.GONE
            }
            PlayState.Pause -> {
                playTrackBtn.visibility = View.VISIBLE
                pauseTrackBtn.visibility = View.GONE
            }
            PlayState.Play -> {
                playTrackBtn.visibility = View.GONE
                pauseTrackBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun setTrackInfo(track: Track?) {
        if (track != null) {
            playerViewModel.setUrl(track.previewUrl)
            setImage(track.artworkUrl100, trackArtWork)
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTimeMillis
            collectionName.text = track.collectionName
            releaseDate.text = Instant.parse(track.releaseDate)
                .atZone(ZoneId.of("UTC"))
                .year.toString()
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun setImage(uri: String, view: ImageView) {
        Glide.with(applicationContext)
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
        backBtn.setOnClickListener {
            finish()
        }
        playTrackBtn.setOnClickListener {
            onPlayBtnClick()
        }
        pauseTrackBtn.setOnClickListener {
            onPauseBtnClick()
        }
    }

    private fun onPlayBtnClick() {
        playerViewModel.start()
    }

    private fun onPauseBtnClick() {
        playerViewModel.pause()
    }

    private fun setViews() {
        backBtn = findViewById(R.id.back_btn)
        trackArtWork = findViewById(R.id.track_artwork)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        addToPlaylistBtn = findViewById(R.id.add_to_playlist_btn)
        playTrackBtn = findViewById(R.id.play_btn)
        pauseTrackBtn = findViewById<ImageButton>(R.id.pause_btn)
        addToFavBtn = findViewById(R.id.add_to_fav_btn)
        trackTime = findViewById(R.id.track_time)
        collectionName = findViewById(R.id.collection_name)
        releaseDate = findViewById(R.id.release_date)
        primaryGenreName = findViewById(R.id.primary_genre_name)
        country = findViewById(R.id.country)
        trackTimeNow = findViewById<TextView>(R.id.track_time_now)
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