package com.example.playlist_maker_dev.player.ui

import com.example.playlist_maker_dev.domain.models.Track

sealed interface AudioPlayerState {
    object Loading : AudioPlayerState
    data class Error(val message: String) : AudioPlayerState
    data class Content(val data: Track) : AudioPlayerState
}