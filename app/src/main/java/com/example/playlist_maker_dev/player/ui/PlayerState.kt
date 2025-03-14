package com.example.playlist_maker_dev.player.ui

sealed class PlayerState(val buttonState: Boolean, val buttonText: String, val progress: String) {

    class Default : PlayerState(false, "PLAY", "00:00")

    class Prepared : PlayerState(false, "PLAY", "00:00")

    class Playing(progress: String) : PlayerState(true, "PAUSE", progress)

    class Paused(progress: String) : PlayerState(false, "PLAY", progress)
}