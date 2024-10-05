package com.example.playlist_maker_dev.search.domain.api

import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.Resource


interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>?>, errorMessage: String?)
    }
}