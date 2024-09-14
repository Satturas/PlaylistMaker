package com.example.playlist_maker_dev.search.domain.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val primaryGenreName: String,
    val releaseDate: String?,
    val country: String,
    val previewUrl: String
) : java.io.Serializable


