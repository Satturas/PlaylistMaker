package com.example.playlist_maker_dev.player.ui

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.ui.search.SearchActivity
import com.example.playlist_maker_dev.ui.search.dpToPx

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor
    private lateinit var mainThreadHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbar.setOnClickListener { finish() }

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory("123")
        )[AudioPlayerViewModel::class.java]

        val trackId = intent.getIntExtra(TRACK_ID, -1)
        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(trackId.toString())
        )[AudioPlayerViewModel::class.java]

        viewModel.getState().observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.Loading -> showLoading()
            is AudioPlayerState.Error -> showError(state.message)
            is AudioPlayerState.Content -> showPlayer(state.data)
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        onBackPressedDispatcher.onBackPressed()
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

        url = track.previewUrl
        binding.progressBar.isVisible = false
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(audioPlayerInteractor.setCurrentPosition())
    }

    companion object {
        private const val TRACK_ID = "track_id"

        fun show(context: Context, trackId: String) {
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_ID, trackId)

            context.startActivity(intent)
        }
    }
}


        /*val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER, Track::class.java)
        } else {
            intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER) as Track
        }

        if (track != null) {
            binding.songTitle.text = track.trackName
            binding.bandTitle.text = track.artistName
            binding.songLength.text = track.trackTimeMillis

            if (track.collectionName?.isNotEmpty() == true) {
                binding.album.text = track.collectionName
                binding.album.visibility = View.VISIBLE
                binding.albumTitle.visibility = View.VISIBLE
            }

            if (track.releaseDate?.isNotEmpty() == true) {
                binding.year.text = track.releaseDate.substring(0, 4)
            }
            binding.genre.text = track.primaryGenreName
            binding.country.text = track.country
            Glide
                .with(binding.imageCover)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.vector_cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
                .into(binding.imageCover)

            url = track.previewUrl
        }

        mainThreadHandler = Handler(Looper.getMainLooper())

        audioPlayerInteractor = Creator.provideAudioPlayerInteractor(
            mediaPlayer,
            mainThreadHandler,
            track,
            binding.playButton,
            binding.songTime
        )
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(audioPlayerInteractor.setCurrentPosition())
    }



}*/