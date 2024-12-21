package com.example.playlist_maker_dev.media.domain.models

data class Playlist
    (
    val id: Int,
    val name: String,
    val description: String?,
    val coverUrl: String?,
    val trackIdsList: List<Int>,
    val tracksQuantity: Int,
    val tracksLength: Int
)
