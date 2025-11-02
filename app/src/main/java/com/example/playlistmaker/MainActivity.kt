package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(R.dimen.padding_16)
            v.setPadding(padding, systemBars.top, padding, systemBars.bottom)
            insets
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val searchBtn = findViewById<Button>(R.id.search_btn)
        val libraryBtn = findViewById<Button>(R.id.library_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)

        searchBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onSearchClick()
            }
        })

        libraryBtn.setOnClickListener { onLibraryClick() }

        settingsBtn.setOnClickListener { onSettingsClick() }
    }

    private fun onSearchClick() {
        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
    }

    private fun onLibraryClick() {
        startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
    }

    private fun onSettingsClick() {
        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
    }
}