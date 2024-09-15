package com.example.playlist_maker_dev.search.domain.api

import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.Resource


interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    //fun loadTrackData(trackId: String, consumer: Consumer)

    interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>?>, errorMessage: String?)
    }
}