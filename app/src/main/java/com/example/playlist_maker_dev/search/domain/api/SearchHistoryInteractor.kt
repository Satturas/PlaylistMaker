package com.example.playlist_maker_dev.search.domain.api

import com.example.playlist_maker_dev.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistoryOfTracks(): MutableList<Track>
    fun saveHistoryOfTracks(param: List<Track>)
    fun saveTrackToHistory (param: Track)
}