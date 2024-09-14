package com.example.playlist_maker_dev.search.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.player.ui.AudioPlayerViewModel
import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.models.Track

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val searchState = MutableLiveData<SearchState>()


    fun showHistoryOfTracks() {
        searchState.value = SearchState.SearchHistoryTracksContent(searchHistoryInteractor.getHistoryOfTracks())
    }

    fun getHistoryOfTracks() = searchHistoryInteractor.getHistoryOfTracks()

    fun saveHistoryOfTracks(list: List<Track>) = searchHistoryInteractor.saveHistoryOfTracks(list)

    fun getState(): LiveData<SearchState> = searchState

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideSearchHistoryInteractor(application),
                    Creator.provideTracksInteractor(application)
                )
            }
        }
    }

}