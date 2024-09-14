package com.example.playlist_maker_dev.domain.api

import com.example.playlist_maker_dev.domain.models.Track


interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    //fun loadTrackData(trackId: String, consumer: Consumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}