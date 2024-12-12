package com.example.playlist_maker_dev.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _playlistsState = MutableLiveData<PlaylistsState>()
    val playlistsState: LiveData<PlaylistsState> = _playlistsState

    fun showPlaylists() {
        _playlistsState.value = PlaylistsState.Loading
        viewModelScope.launch {
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
        _playlistsState.postValue(state)
    }
}