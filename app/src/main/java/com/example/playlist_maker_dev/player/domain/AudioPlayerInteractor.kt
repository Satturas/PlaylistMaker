package com.example.playlist_maker_dev.player.domain

interface AudioPlayerInteractor {
    fun preparePlayer()
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun setCurrentPosition(): Runnable
}