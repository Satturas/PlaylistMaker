package com.example.playlist_maker_dev.search.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.Resource

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> = _searchState

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchTracks(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun showHistoryOfTracks() {
        _searchState.value = SearchState.Loading
        _searchState.value =
            SearchState.SearchHistoryTracksContent(searchHistoryInteractor.getHistoryOfTracks())
    }

    private fun searchTracks(query: String) {
        if (query.isNotEmpty()) {
            renderState(SearchState.Loading)

            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(foundTracks: Resource<List<Track>?>, errorMessage: String?) {
                    val tracks = mutableListOf<Track>()
                    foundTracks.data?.let { tracks.addAll(it) }
                    when (foundTracks) {
                        is Resource.Error -> renderState(SearchState.Error(errorMessage.toString()))
                        is Resource.Success -> {
                            if (!foundTracks.data.isNullOrEmpty()) {
                                renderState(SearchState.FoundTracksContent(foundTracks.data))
                            } else renderState(SearchState.NothingFound)
                        }
                    }
                }
            })
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

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}


