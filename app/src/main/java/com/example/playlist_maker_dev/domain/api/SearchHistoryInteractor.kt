package com.example.playlist_maker_dev.domain.api

import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository

interface SearchHistoryInteractor {
    fun getHistoryOfTracks(): MutableList<Track>
    fun saveHistoryOfTracks(param: List<Track>)
    fun saveTrackToHistory (param: Track)
}