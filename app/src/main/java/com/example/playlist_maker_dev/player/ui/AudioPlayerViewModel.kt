package com.example.playlist_maker_dev.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
) : ViewModel() {

    private val _playerState = MutableLiveData<AudioPlayerState>()
    val playerState: LiveData<AudioPlayerState> get() = _playerState

    private val _currentSongTime = MutableLiveData(DEFAULT_CURRENT_POS)
    val currentSongTime: LiveData<Int> get() = _currentSongTime

    private var timerJob: Job? = null

    init {
        _playerState.value = AudioPlayerState.STATE_DEFAULT
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

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_playerState.value == AudioPlayerState.STATE_PLAYING) {
                delay(DELAY)
                _currentSongTime.postValue(interactor.getCurrentSongTime())
            }
        }
    }

    companion object {
        private const val DELAY = 500L
        private const val DEFAULT_CURRENT_POS = 0
    }
}