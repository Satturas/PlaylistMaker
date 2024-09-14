package com.example.playlist_maker_dev.search.data.network

import com.example.playlist_maker_dev.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}