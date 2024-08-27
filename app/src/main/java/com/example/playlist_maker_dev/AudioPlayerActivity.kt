package com.example.playlist_maker_dev

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var url: String
    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val backButton = findViewById<Toolbar>(R.id.toolbar)
        backButton.setOnClickListener {
            finish()
        }

        val emptyTrack: Track?

        if (intent.hasExtra(SearchActivity.AUDIO_PLAYER)) {

            emptyTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER, Track::class.java)
            } else {
                intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER) as Track
            }

            if (emptyTrack != null) {
                binding.songTitle.text = emptyTrack.trackName
                binding.bandTitle.text = emptyTrack.artistName
                binding.songLength.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(emptyTrack.trackTimeMillis.toLong())

                if (emptyTrack.collectionName?.isNotEmpty() == true) {
                    binding.album.text = emptyTrack.collectionName
                    binding.album.visibility = View.VISIBLE
                    binding.albumTitle.visibility = View.VISIBLE
                }

                if (emptyTrack.releaseDate?.isNotEmpty() == true) {
                    binding.year.text = emptyTrack.releaseDate.substring(0, 4)
                }
                binding.genre.text = emptyTrack.primaryGenreName
                binding.country.text = emptyTrack.country
                Glide
                    .with(binding.imageCover)
                    .load(emptyTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.vector_cover_placeholder)
                    .centerCrop()
                    .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
                    .into(binding.imageCover)

                url = emptyTrack.previewUrl
            }
        }

        preparePlayer()
        binding.playButton.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(
                setCurrentPosition()
            )
        }

        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacks(setCurrentPosition())
    }


    fun preparePlayer() {
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
        private const val DELAY = 400L
    }
}
