package com.example.playlist_maker_dev.ui.player

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.Creator
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.domain.api.AudioPlayerInteractor
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.ui.search.SearchActivity
import com.example.playlist_maker_dev.ui.search.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor
    private lateinit var mainThreadHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val backButton = findViewById<Toolbar>(R.id.toolbar)
        backButton.setOnClickListener {
            finish()
        }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

        audioPlayerInteractor = Creator.provideAudioInteractor(
            mediaPlayer,
            mainThreadHandler,
            track,
            binding.playButton,
            binding.songTime
        )



        /*preparePlayer()
        binding.playButton.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(
                setCurrentPosition()
            )
        }*/


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


    /*fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.vector_play)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.vector_pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.vector_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun setCurrentPosition(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        binding.songTime.text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler?.postDelayed(this, DELAY)
                    }

                    STATE_PAUSED -> {
                        mainThreadHandler?.removeCallbacks(this)

                    }

                    STATE_PREPARED -> {
                        binding.songTime.text = getString(R.string.timer_00)
                    }

                }
            }
        }
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }*/
}
