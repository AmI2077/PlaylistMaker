package com.example.playlistmaker.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addTrackBottomSheetFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                }
                R.id.addPlaylistFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                }
                R.id.playlistDetailsFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                }
                R.id.playerFragment -> {
                    binding.bottomNavView.menu.findItem(R.id.searchFragment).isChecked = true
                    binding.bottomNavView.visibility = View.GONE
                }
                else ->  binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }
}