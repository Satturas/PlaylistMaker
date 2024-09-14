package com.example.playlist_maker_dev.player.data

import android.media.MediaPlayer
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository
import com.example.playlist_maker_dev.player.ui.AudioPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerRepositoryImpl() : AudioPlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var playerState: AudioPlayerState = AudioPlayerState.STATE_DEFAULT

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun preparePlayer(track: Track?, callback: (AudioPlayerState) -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track?.previewUrl.toString())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            callback.invoke(AudioPlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            callback.invoke(AudioPlayerState.STATE_PREPARED)
        }
    }

    override fun startPlayer() = mediaPlayer.start()

    override fun pausePlayer() = mediaPlayer.pause()

    override fun stopPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun getCurrentSongTime(): Int = mediaPlayer.currentPosition

}