package com.example.playlist_maker_dev.search.domain.impl

import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) :
    SearchHistoryInteractor {
    override fun getHistoryOfTracks(): Flow<MutableList<Track>> = flow {
        searchHistoryRepository.getSearchHistory().collect { emit(it) }
    }

    override fun saveHistoryOfTracks(
        param: List<Track>
    ) = searchHistoryRepository.saveSearchHistory(param)

    override fun saveTrackToHistory(
        param: Track
    ) = searchHistoryRepository.saveTrackToHistory(param)
}