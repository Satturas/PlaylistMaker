package com.example.playlist_maker_dev.domain.repository

import com.example.playlist_maker_dev.domain.models.Track

interface SearchHistoryRepository {

    fun saveSearchHistory(param: List<Track>)

    fun getSearchHistory(): MutableList<Track>

    fun saveTracktoHistory(param: Track)

}