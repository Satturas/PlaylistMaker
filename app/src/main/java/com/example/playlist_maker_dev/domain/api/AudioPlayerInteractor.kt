package com.example.playlist_maker_dev.domain.api

interface AudioPlayerInteractor {
    fun preparePlayer()
    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun setCurrentPosition(): Runnable
}