package com.example.playlist_maker_dev.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.databinding.ActivityMainBinding
import com.example.playlist_maker_dev.presentation.App
import com.example.playlist_maker_dev.search.ui.SearchViewModel


class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainScreenViewModel.getViewModelFactory(this)
        )[MainScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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