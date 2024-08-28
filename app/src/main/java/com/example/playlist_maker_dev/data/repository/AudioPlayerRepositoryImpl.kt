package com.example.playlist_maker_dev.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.AudioPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val handler: Handler,
    val track: Track?,
    private val playImageView: ImageView,
    private val trackTimerTextView: TextView
) : AudioPlayerRepository {

    private var playerState = STATE_DEFAULT
    private var url: String

    init {
        url = track?.previewUrl.toString()
        preparePlayer()
        playImageView.setOnClickListener {
            playbackControl()
            handler.post(
                setCurrentPosition()
            )
        }
    }

    override fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playImageView.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playImageView.setImageResource(R.drawable.vector_play)
            playerState = STATE_PREPARED
        }
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playImageView.setImageResource(R.drawable.vector_pause_button)
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playImageView.setImageResource(R.drawable.vector_play)
        playerState = STATE_PAUSED
    }

    override fun setCurrentPosition(): Runnable {
        val mainThreadHandler = Handler(Looper.getMainLooper())
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        trackTimerTextView.text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler.postDelayed(this, DELAY)
                    }

                    STATE_PAUSED -> {
                        mainThreadHandler.removeCallbacks(this)

                    }

                    STATE_PREPARED -> {
                        trackTimerTextView.text = R.string.timer_00.toString()
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
    }
}