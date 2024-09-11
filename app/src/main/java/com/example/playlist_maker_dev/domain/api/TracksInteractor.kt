package com.example.playlist_maker_dev.domain.api

import com.example.playlist_maker_dev.domain.models.Track

interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    fun loadTrackData(trackId: String, onComplete: Any)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
        fun onFailure(t: Throwable)
    }
}