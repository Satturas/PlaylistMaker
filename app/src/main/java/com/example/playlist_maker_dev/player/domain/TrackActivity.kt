package com.example.playlist_maker_dev.player.domain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.databinding.ActivitySearchBinding
import com.example.playlist_maker_dev.player.data.PlayStatus
import com.example.playlist_maker_dev.player.data.TrackScreenState


class TrackActivity : ComponentActivity() {
    private val viewModel by viewModels<TrackViewModel> { TrackViewModel.getViewModelFactory("123") }
    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Content -> {
                    changeContentVisibility(loading = false)
                    binding.imageCover.setImage(screenState.trackModel.pictureUrl)
                    binding.bandTitle.text = screenState.trackModel.author
                    binding.songTitle.text = screenState.trackModel.name
                }
                is TrackScreenState.Loading -> {
                    changeContentVisibility(loading = true)
                }
            }
        }
        // 1
        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
        }
    }

    private fun changeContentVisibility(loading: Boolean) {
        binding.progressBar.isVisible = loading

        binding.playButton.isVisible = !loading
        binding.imageCover.isVisible = !loading
        binding.bandTitle.isVisible = !loading
        binding.songTitle.isVisible = !loading
    }

    private fun changeButtonStyle(playStatus: PlayStatus) {
        // Меняем вид кнопки проигрывания в зависимости от того, играет сейчас трек или нет
    }
}