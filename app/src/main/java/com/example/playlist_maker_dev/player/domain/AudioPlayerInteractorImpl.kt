package com.example.playlist_maker_dev.player.domain

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun preparePlayer() {
        audioPlayerRepository.preparePlayer()
    }

    override fun playbackControl() {
        audioPlayerRepository.playbackControl()
    }

    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun setCurrentPosition(): Runnable {
        return audioPlayerRepository.setCurrentPosition()
    }
}