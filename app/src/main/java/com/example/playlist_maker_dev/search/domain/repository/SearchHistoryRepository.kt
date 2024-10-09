package com.example.playlist_maker_dev.search.domain.repository

import com.example.playlist_maker_dev.search.domain.models.Track

interface SearchHistoryRepository {

    fun saveSearchHistory(param: List<Track>)

    fun getSearchHistory(): MutableList<Track>

    fun saveTrackToHistory(param: Track)

}