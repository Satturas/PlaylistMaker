package com.example.playlist_maker_dev.search.data.dto

data class TrackDto (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val primaryGenreName: String,
    val releaseDate: String?,
    val country: String,
    val previewUrl: String
)
