package com.example.playlistmaker.ui

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.libraryScreen.LibraryActivity
import com.example.playlistmaker.ui.searchScreen.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var searchBtn: Button
    private lateinit var libraryBtn: Button
    private lateinit var settingsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        searchBtn = findViewById<Button>(R.id.search_btn)
        libraryBtn = findViewById<Button>(R.id.library_btn)
        settingsBtn = findViewById<Button>(R.id.settings_btn)

        setupClickListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.horizontalScreenPadding)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.verticalScreenPadding)
            v.setPadding(horizontalPadding, systemBars.top + verticalPadding, horizontalPadding, systemBars.bottom)
            insets
        }
    }

    private fun setupClickListeners() {
        searchBtn.setOnClickListener { onSearchBtnClick() }
        libraryBtn.setOnClickListener { onLibraryBtnClick() }
        settingsBtn.setOnClickListener { onSettingsBtnClick() }
    }

    private fun onSearchBtnClick() {
        startActivity(Intent(this, SearchActivity::class.java))

    }

    private fun onLibraryBtnClick() {
        startActivity(Intent(this, LibraryActivity::class.java))
    }

    private fun onSettingsBtnClick() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}