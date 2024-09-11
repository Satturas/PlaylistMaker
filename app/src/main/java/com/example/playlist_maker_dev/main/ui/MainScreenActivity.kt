package com.example.playlist_maker_dev.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.databinding.ActivityMainBinding
import com.example.playlist_maker_dev.ui.media.MediaActivity
import com.example.playlist_maker_dev.ui.settings.SettingsActivity


class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            MainScreenViewModelFactory(this)
        ).get(MainScreenViewModel::class.java)

        binding.buttonSearch.setOnClickListener {
            viewModel.onSearchClick()
        }

        binding.buttonMedia.setOnClickListener {
            viewModel.onMediaClick()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.onSettingsClick()
        }
    }
}