package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.horizontalScreenPadding)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.verticalScreenPadding)
            v.setPadding(horizontalPadding, systemBars.top + verticalPadding, horizontalPadding, systemBars.bottom)
            insets
        }

        binding.viewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        getTabLayoutMediator().attach()

        binding.backBtn.setOnClickListener { finish() }
    }


    private fun getTabLayoutMediator(): TabLayoutMediator =
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = this.getString(R.string.fav_tracks)
                1 -> tab.text = this.getString(R.string.playlists)
            }
        }
}