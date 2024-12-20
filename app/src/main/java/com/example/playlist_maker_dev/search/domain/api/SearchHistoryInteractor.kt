package com.example.playlist_maker_dev.search.domain.api

import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    fun getHistoryOfTracks(): Flow<MutableList<Track>>
    fun saveHistoryOfTracks(param: List<Track>)
    fun saveTrackToHistory (param: Track)
}