package com.example.playlist_maker_dev.services

import com.example.playlist_maker_dev.player.ui.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showForeground()
    fun hideForeground()
}

