package com.example.playlist_maker_dev.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.search.domain.models.Track

class FavoriteTracksViewModel(private val track: Track) : ViewModel() {
    private val _trackLiveData = MutableLiveData(track)
    val trackLiveData: LiveData<Track> = _trackLiveData
}
