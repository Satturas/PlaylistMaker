package com.example.playlist_maker_dev.player.data

import android.media.MediaPlayer
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository
import com.example.playlist_maker_dev.player.ui.AudioPlayerState
import com.example.playlist_maker_dev.search.domain.models.Track

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    override fun preparePlayer(track: Track?, callback: (AudioPlayerState) -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track?.previewUrl)
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

    override fun stopPlayer() = mediaPlayer.stop()

    override fun getCurrentSongTime(): Int = mediaPlayer.currentPosition

}