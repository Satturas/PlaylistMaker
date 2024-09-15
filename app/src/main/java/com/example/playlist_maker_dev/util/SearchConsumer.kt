package com.example.playlist_maker_dev.util

interface SearchConsumer <T> {
    fun consume(data: ResourceData<T>)
}