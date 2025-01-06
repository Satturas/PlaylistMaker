package com.example.playlist_maker_dev.media.ui.playlist_screen

import android.util.Log
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
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist
    val track: Track? = null

    private val _playlistTracks = MutableLiveData<List<Track>>()
    val playlistTracks: LiveData<List<Track>> = _playlistTracks

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistById(playlistId).collect { playlist ->
                _playlist.postValue(playlist)
                Log.e("playlist", playlist.toString())
                if (playlist.trackIdsList.isNotEmpty()) {
                    playlistsInteractor.getTracksById(playlist.trackIdsList).collect { tracks ->
                        val trackList = mutableListOf<Track>()
                        Log.e("trackIdsList", playlist.trackIdsList.toString())
                        playlist.trackIdsList.forEach { trackId ->
                            tracks.find { track -> track.trackId == trackId }
                                ?.let { trackList.add(0, it) }
                        }
                        _playlistTracks.postValue(trackList)
                        Log.e("tracksList", trackList.toString())
                    }
                }
            }
        }
    }

    fun removeTrackFromPlaylist(trackId: Int, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deleteTrackFromPlaylist(trackId, playlistId)
            getPlaylistById(playlistId)
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


