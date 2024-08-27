package com.example.playlist_maker_dev.data.network

import com.example.playlist_maker_dev.data.dto.Response
import com.example.playlist_maker_dev.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = iTunesService.searchTracks(dto.term).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}