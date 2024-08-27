package com.example.playlist_maker_dev.data.network

import com.example.playlist_maker_dev.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}