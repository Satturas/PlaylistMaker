package com.example.playlist_maker_dev

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Track(
    val trackID: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String?
) {
}

class TrackResponse(
    val results: List<Track>
)

interface ITunesApi {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") text: String): Call<TrackResponse>
}