package com.example.playlist_maker_dev.player.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}