package com.example.playlist_maker_dev.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.FavouritesInteractor
import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsState
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<AudioPlayerState>()
    val playerState: LiveData<AudioPlayerState> get() = _playerState

    private val _favouriteState = MutableLiveData<Boolean>()
    val favouriteState: LiveData<Boolean> get() = _favouriteState

    private val _currentSongTime = MutableLiveData(DEFAULT_CURRENT_POS)
    val currentSongTime: LiveData<Int> get() = _currentSongTime

    private val _playlistsState = MutableLiveData<PlaylistsState>()
    val playlistsState: LiveData<PlaylistsState> = _playlistsState

    private var timerJob: Job? = null

    init {
        _playerState.value = AudioPlayerState.STATE_DEFAULT
    }

    fun renderFavState(track: Track) {
        viewModelScope.launch {
            favouritesInteractor.isTrackFavourite(track).collect {
                _favouriteState.postValue(!it)
            }
        }
    }

    fun preparePlayer(track: Track?) = interactor.preparePlayer(track) { state ->
        if (state == AudioPlayerState.STATE_PREPARED) {
            timerJob?.cancel()
            _playerState.value = AudioPlayerState.STATE_PREPARED
        }
    }

    fun startPlayer() {
        interactor.startPlayer()
        _playerState.value = AudioPlayerState.STATE_PLAYING
        startTimer()
    }

    fun pausePlayer() {
        interactor.pausePlayer()
        _playerState.value = AudioPlayerState.STATE_PAUSED
        timerJob?.cancel()
    }

    fun stopPlayer() {
        interactor.stopPlayer()
        _playerState.value = AudioPlayerState.STATE_STOPPED
    }

    fun onFavouriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorite) {
                favouritesInteractor.removeTrackFromFavourites(track.trackId)
                track.isFavorite = false
            } else {
                favouritesInteractor.addTrackToFavourites(track)
                track.isFavorite = true
            }
            _favouriteState.postValue(!track.isFavorite)
        }
    }

    fun onAddToPlaylistClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect { playlists ->
                processResult(playlists)
            }
        }
    }

    fun trackIsInPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlist.trackIdsList.contains(track.trackId)
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.addTrackToPlaylist(track, playlist)
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        renderState(PlaylistsState.FoundPlaylistsContent(playlists))
    }

    private fun renderState(state: PlaylistsState) {
        _playlistsState.postValue(state)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_playerState.value == AudioPlayerState.STATE_PLAYING) {
                delay(DELAY)
                _currentSongTime.postValue(interactor.getCurrentSongTime())
            }
        }
    }

    companion object {
        private const val DELAY = 300L
        private const val DEFAULT_CURRENT_POS = 0
    }
}