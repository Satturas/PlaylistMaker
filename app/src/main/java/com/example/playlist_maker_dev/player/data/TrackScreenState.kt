package com.example.playlist_maker_dev.player.data

sealed class TrackScreenState {
    object Loading: TrackScreenState()
    data class Content(
        val trackModel: TrackModel,
    ): TrackScreenState()
}