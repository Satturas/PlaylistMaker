package com.example.playlist_maker_dev.search.ui

import com.example.playlist_maker_dev.search.domain.models.Track

sealed interface SearchState {
    data object NothingFound : SearchState
    data object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class FoundTracksContent(val foundTracks: List<Track>) : SearchState
    data class SearchHistoryTracksContent(val searchHistoryTracks: List<Track>) : SearchState

}