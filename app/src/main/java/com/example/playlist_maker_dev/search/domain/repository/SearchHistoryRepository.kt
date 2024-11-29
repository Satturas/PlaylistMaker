package com.example.playlist_maker_dev.search.domain.repository

import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun saveSearchHistory(param: List<Track>)

    fun getSearchHistory(): Flow<MutableList<Track>>

    fun saveTrackToHistory(param: Track)

}