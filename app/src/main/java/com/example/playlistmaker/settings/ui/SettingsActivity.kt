package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.di.ServiceCreator
import com.example.playlistmaker.main.App

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModels {
        ServiceCreator.createSettingsViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.horizontalScreenPadding)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.verticalScreenPadding)
            v.setPadding(
                horizontalPadding,
                systemBars.top + verticalPadding,
                horizontalPadding,
                systemBars.bottom
            )
            insets
        }

        viewModel.themeState.observe(this) { themeState ->
            render(themeState)
        }

        setupClickListeners()
        setChangeListener()
    }

    private fun render(themeState: ThemeState) {
        binding.themeSwitcher.isChecked = (application as App).switchTheme(themeState)
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener { finish() }
        binding.shareBtn.setOnClickListener { onShareBtnClick() }
        binding.supportBtn.setOnClickListener { onSupportBtnClick() }
        binding.agreementBtn.setOnClickListener { onUserAgreementBtnClick() }
    }

    private fun setChangeListener() {
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            switchTheme(isChecked)
        }
    }

    private fun switchTheme(isChecked: Boolean) {
        if (isChecked) viewModel.onEvent(Event.SetDarkTheme)
        else viewModel.onEvent(Event.SetLightTheme)
    }

    private fun onShareBtnClick() {
        viewModel.onEvent(Event.ShareApp)
    }

    private fun onSupportBtnClick() {
        viewModel.onEvent(Event.SendEmail)
    }

    private fun onUserAgreementBtnClick() {
        viewModel.onEvent(Event.OpenTerms)
    }
}