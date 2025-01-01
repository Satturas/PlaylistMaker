package com.example.playlist_maker_dev.media.ui.playlist_screen

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val application: Application,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _playlistTracks = MutableLiveData<List<Track>>()
    val playlistTracks: LiveData<List<Track>> = _playlistTracks

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistById(playlistId).collect { playlist ->
                _playlist.postValue(playlist)
            }
        }
    }

    fun getTracks(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getTracksOfPlaylist(playlistId).collect { trackIds ->
                val trackList = mutableListOf<Track>()
                for (i in trackIds) {
                    viewModelScope.launch(Dispatchers.IO) {
                        playlistsInteractor.getTrackById(i).collect { track ->
                            trackList.add(track)
                        }
                    }
                }
                _playlistTracks.postValue(trackList)
            }
        }
    }

    fun removeTrackFromPlaylist(trackId: Int, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deleteTrackFromPlaylist(trackId, playlistId)
            getTracks(playlistId)
        }
    }

    fun sharePlaylistToOtherApps(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.sharePlaylistToOtherApps(playlistId)
        }
    }

    fun removePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deletePlaylist(playlistId)
        }
    }

}


