package com.example.playlist_maker_dev.media.ui.media_fav_tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.FavouritesInteractor
import com.example.playlist_maker_dev.media.ui.media_root.MediaState
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Default)
    val mediaState: StateFlow<MediaState> = _mediaState

    private val _isTrackListVisible = MutableStateFlow(false)
    val isTrackListVisible: StateFlow<Boolean> = _isTrackListVisible

    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            favouritesInteractor
                .getFavouriteTracks()
                .collect { tracks -> processResult(tracks) }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(MediaState.NothingInFavourite)
        } else {
            renderState(MediaState.FavouriteTracks(tracks))
            _isTrackListVisible.value = true
        }
    }

    private fun renderState(state: MediaState) {
        _mediaState.value = state
    }
}
