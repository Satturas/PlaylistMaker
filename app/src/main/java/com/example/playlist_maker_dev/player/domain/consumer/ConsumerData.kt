package com.example.playlist_maker_dev.player.domain.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: T) : ConsumerData<T>
}