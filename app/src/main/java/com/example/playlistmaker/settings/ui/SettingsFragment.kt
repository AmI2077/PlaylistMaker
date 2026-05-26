package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.main.App
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeState.observe(viewLifecycleOwner) { themeState ->
            render(themeState)
        }

        setupClickListeners()
        setChangeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(themeState: ThemeState) {
        binding.themeSwitcher.isChecked = (requireContext().applicationContext as App).switchTheme(themeState)
    }

    private fun setupClickListeners() {
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