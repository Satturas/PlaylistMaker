package com.example.playlist_maker_dev.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker_dev.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainScreenViewModel>()

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