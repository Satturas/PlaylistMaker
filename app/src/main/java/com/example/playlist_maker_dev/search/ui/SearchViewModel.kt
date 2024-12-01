package com.example.playlist_maker_dev.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> = _searchState

    private var latestSearchText: String? = null

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchTracks(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun showHistoryOfTracks() {
        _searchState.value = SearchState.Loading
        viewModelScope.launch {
            searchHistoryInteractor.getHistoryOfTracks().collect { tracks ->
                processResult(tracks)
            }
        }
    }

    private fun processResult(tracks: List<Track>) {
        renderState(SearchState.SearchHistoryTracksContent(tracks))
    }


    private fun searchTracks(query: String) {
        if (query.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> renderState(SearchState.Error(errorMessage.toString()))
            tracks.isEmpty() -> renderState(SearchState.NothingFound)
            else -> renderState(SearchState.FoundTracksContent(tracks))
        }
    }

    fun saveTrackToHistory(track: Track) {
        searchHistoryInteractor.saveTrackToHistory(track)
    }

    fun saveHistoryOfTracks(list: List<Track>) {
        searchHistoryInteractor.saveHistoryOfTracks(list)
    }

    private fun renderState(state: SearchState) {
        _searchState.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}


