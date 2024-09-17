package com.example.playlist_maker_dev.player.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.SearchActivity
import com.example.playlist_maker_dev.search.ui.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbar.setOnClickListener { finish() }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER, Track::class.java)
        } else {
            intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER) as Track
        }

        showLoading()

        if (track != null) {
            showPlayer(track)

            viewModel = ViewModelProvider(
                this,
                AudioPlayerViewModel.getViewModelFactory()
            )[AudioPlayerViewModel::class.java]

            viewModel.preparePlayer(track)
        }

        viewModel.playerState.observe(this, Observer { playerState ->

            when (playerState) {

                AudioPlayerState.STATE_PREPARED -> {
                    binding.songTime.setText(R.string.timer_00)
                    binding.playButton.setImageResource(R.drawable.vector_play)
                }

                AudioPlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.vector_pause_button)

                }

                AudioPlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.vector_play)
                }

                else -> {}
            }

        })

        viewModel.currentSongTime.observe(this, Observer { time ->
            binding.songTime.text = dateFormat.format(time)
        })

        binding.playButton.setOnClickListener {
            if (viewModel.playerState.value == AudioPlayerState.STATE_PLAYING) {
                viewModel.pausePlayer()
            } else {
                viewModel.startPlayer()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun showPlayer(track: Track) {
        binding.songTitle.text = track.trackName
        binding.bandTitle.text = track.artistName
        binding.songLength.text = track.trackTimeMillis
        binding.album.text = track.collectionName
        binding.album.visibility = View.VISIBLE
        binding.albumTitle.visibility = View.VISIBLE
        binding.year.text = track.releaseDate?.substring(0, 4) ?: ""
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

        Glide
            .with(binding.imageCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.vector_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
            .into(binding.imageCover)

        binding.progressBar.isVisible = false
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

