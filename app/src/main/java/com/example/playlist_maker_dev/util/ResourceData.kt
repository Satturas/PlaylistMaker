package com.example.playlist_maker_dev.util

sealed interface ResourceData<T> {
    data class Data<T>(val value: T) : ResourceData<T>
    data class Error<T>(val resultCode: Int) : ResourceData<T>
}