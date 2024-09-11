package com.example.playlist_maker_dev.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivitySettingsBinding
import com.example.playlist_maker_dev.presentation.App
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModelFactory(this)
        ).get(SettingsViewModel::class.java)

        binding.buttonWriteToSupport.setOnClickListener { viewModel.writeToSupport() }
        binding.buttonUserAgreement.setOnClickListener { viewModel.userAgreement() }
        binding.buttonShareApp.setOnClickListener { viewModel.shareTextToOtherApps() }
        binding.buttonBackFromSettings.setOnClickListener { finish() }
        binding.themeSwitcher

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}