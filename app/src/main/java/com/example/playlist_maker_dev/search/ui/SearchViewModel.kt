package com.example.playlist_maker_dev.search.ui

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.Resource

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val searchState = MutableLiveData<SearchState>()
    private var latestSearchText: String? = null
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
        searchState.value = SearchState.Loading
        searchState.value =
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

    fun saveTracktoHistory(track: Track) = searchHistoryInteractor.saveTrackToHistory(track)

    fun getHistoryOfTracks() = searchHistoryInteractor.getHistoryOfTracks()

    fun saveHistoryOfTracks(list: List<Track>) =
        searchHistoryInteractor.saveHistoryOfTracks(list)

    fun getState(): LiveData<SearchState> = searchState

    private fun renderState(state: SearchState) {
        searchState.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        Creator.provideSearchHistoryInteractor(),
                        Creator.provideTracksInteractor()
                    )
                }
            }
    }
}


