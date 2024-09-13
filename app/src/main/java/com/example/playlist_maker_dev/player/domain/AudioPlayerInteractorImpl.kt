package com.example.playlist_maker_dev.player.domain

import com.example.playlist_maker_dev.domain.models.Track

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun preparePlayer(track: Track?) {
        audioPlayerRepository.preparePlayer(track)
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