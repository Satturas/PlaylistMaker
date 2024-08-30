package com.example.playlist_maker_dev.domain.impl

import com.example.playlist_maker_dev.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getHistoryOfTracks(): MutableList<Track> {
        return searchHistoryRepository.getSearchHistory()
    }

    override fun saveHistoryOfTracks(
        param: List<Track>
    ) = searchHistoryRepository.saveSearchHistory(param)

    override fun saveTrackToHistory(
        param: Track
    ) = searchHistoryRepository.saveTracktoHistory(param)
}