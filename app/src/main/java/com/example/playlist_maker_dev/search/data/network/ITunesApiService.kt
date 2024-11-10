package com.example.playlist_maker_dev.search.data.network

import com.example.playlist_maker_dev.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ITunesApiService {
    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): TracksSearchResponse
}
