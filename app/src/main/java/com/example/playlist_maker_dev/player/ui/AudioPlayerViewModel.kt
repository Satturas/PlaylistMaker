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
import com.example.playlist_maker_dev.services.AudioPlayerControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private var audioPlayerControl: AudioPlayerControl? = null

    private val _favouriteState = MutableLiveData<Boolean>()
    val favouriteState: LiveData<Boolean> get() = _favouriteState

    private val _currentSongTime = MutableLiveData(DEFAULT_CURRENT_POS)
    val currentSongTime: LiveData<Int> get() = _currentSongTime

    private val _playlistsState = MutableLiveData<PlaylistsState>()
    val playlistsState: LiveData<PlaylistsState> = _playlistsState

    init {
        _playerState.value = PlayerState.Default()
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                _playerState.postValue(it)
            }
        }
    }

    fun onPlayerButtonClicked() {
        if (playerState.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun renderFavState(track: Track) {
        viewModelScope.launch {
            favouritesInteractor.isTrackFavourite(track).collect {
                _favouriteState.postValue(!it)
            }
        }
    }

    /*fun preparePlayer(track: Track?) = interactor.preparePlayer(track) { state ->
        if (state == AudioPlayerState.STATE_PREPARED) {
            timerJob?.cancel()
            _playerState.value = PlayerState.Prepared()
        }
    }

    fun startPlayer() {
        interactor.startPlayer()
        _playerState.value = PlayerState.Playing()
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
    }*/

    fun onFavouriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorite) {
                track.isFavorite = false
                favouritesInteractor.removeTrackFromFavourites(track.trackId)
            } else {
                track.isFavorite = true
                favouritesInteractor.addTrackToFavourites(track)
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

    fun showNotification(needToShow: Boolean) {
        if (!needToShow) {
            audioPlayerControl?.hideForeground()
        } else if (playerState.value is PlayerState.Playing) {
            audioPlayerControl?.showForeground()
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        renderState(PlaylistsState.FoundPlaylistsContent(playlists))
    }

    private fun renderState(state: PlaylistsState) {
        _playlistsState.postValue(state)
    }

    /*private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_playerState.value == PlayerState.Playing()) {
                delay(DELAY)
                _currentSongTime.postValue(interactor.getCurrentSongTime())
            }
        }
    }*/

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    companion object {
        private const val DEFAULT_CURRENT_POS = 0
    }
}