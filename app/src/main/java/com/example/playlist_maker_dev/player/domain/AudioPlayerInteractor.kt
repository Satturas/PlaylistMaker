package com.example.playlist_maker_dev.player.domain

import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.player.ui.AudioPlayerState

interface AudioPlayerInteractor {
    fun preparePlayer(track: Track?, callback: (AudioPlayerState) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun getCurrentSongTime(): Int
}