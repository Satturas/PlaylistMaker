package com.example.playlist_maker_dev.media.ui.playlist_screen

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val application: Application,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistById(playlistId).collect { playlist ->
                _playlist.postValue(playlist)
            }
        }
    }
}
