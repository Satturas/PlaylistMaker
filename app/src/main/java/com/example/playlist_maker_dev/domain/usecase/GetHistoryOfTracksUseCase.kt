package com.example.playlist_maker_dev.domain.usecase

import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository

class GetHistoryOfTracksUseCase(private val searchHistoryRepository: SearchHistoryRepository) {
    fun execute(): MutableList<Track> {
        return searchHistoryRepository.getSearchHistory()
    }
}