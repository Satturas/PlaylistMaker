package com.example.playlist_maker_dev.media.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _playlistsState = MutableStateFlow<PlaylistsState>(PlaylistsState.NoPlaylists)
    val playlistsState: StateFlow<PlaylistsState> = _playlistsState

    init {
        showPlaylists()
    }

    private fun showPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect { playlists ->
                processResult(playlists)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>?) {
        if (playlists.isNullOrEmpty()) {
            renderState(PlaylistsState.NoPlaylists)
        } else {
            renderState(PlaylistsState.FoundPlaylistsContent(playlists))
        }
    }

    private fun renderState(state: PlaylistsState) {
        _playlistsState.value = state
    }
}