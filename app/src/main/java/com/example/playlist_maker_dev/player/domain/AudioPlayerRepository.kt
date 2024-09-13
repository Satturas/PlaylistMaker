package com.example.playlist_maker_dev.player.domain

import com.example.playlist_maker_dev.domain.models.Track

interface AudioPlayerRepository {
    fun preparePlayer(track: Track?)
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun setCurrentPosition(): Runnable
}