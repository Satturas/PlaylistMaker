package com.example.playlist_maker_dev.player.domain

import com.example.playlist_maker_dev.player.ui.AudioPlayerState
import com.example.playlist_maker_dev.search.domain.models.Track

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun preparePlayer(track: Track?, callback: (AudioPlayerState) -> Unit) =
        audioPlayerRepository.preparePlayer(track, callback)

    override fun startPlayer() = audioPlayerRepository.startPlayer()

    override fun pausePlayer() = audioPlayerRepository.pausePlayer()

    override fun stopPlayer() = audioPlayerRepository.stopPlayer()

    override fun getCurrentSongTime(): Int = audioPlayerRepository.getCurrentSongTime()

}