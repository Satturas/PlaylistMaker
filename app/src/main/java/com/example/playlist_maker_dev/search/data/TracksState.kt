package com.example.playlist_maker_dev.search.data

import com.example.playlist_maker_dev.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val movies: List<Track>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState

}