package com.example.playlist_maker_dev.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by lazy {
        ViewModelProvider(this,
            SettingsViewModel.getViewModelFactory(this)
        )[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonWriteToSupport.setOnClickListener { viewModel.writeToSupport() }
        binding.buttonUserAgreement.setOnClickListener { viewModel.userAgreement() }
        binding.buttonShareApp.setOnClickListener { viewModel.shareTextToOtherApps() }
        binding.buttonBackFromSettings.setOnClickListener { finish() }

        viewModel.darkThemeEnabled.observe(this) { enabled ->
            if (binding.themeSwitcher.isChecked != enabled) {
                binding.themeSwitcher.isChecked = enabled
            }
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }
}