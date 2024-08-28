package com.example.playlist_maker_dev.domain.usecase

import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository

class SaveTrackToHistoryUseCase (private val searchHistoryRepository: SearchHistoryRepository) {

    fun execute(param: Track) = searchHistoryRepository.saveTracktoHistory(param)

}