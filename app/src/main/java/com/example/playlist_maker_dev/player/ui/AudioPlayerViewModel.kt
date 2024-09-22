package com.example.playlist_maker_dev.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.search.domain.models.Track

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val _playerState = MutableLiveData<AudioPlayerState>()
    val playerState: LiveData<AudioPlayerState> get() = _playerState

    private val _currentSongTime = MutableLiveData(DEFAULT_CURRENT_POS)
    val currentSongTime: LiveData<Int> get() = _currentSongTime

    private val timerRunnable by lazy {
        object : Runnable {
            override fun run() {
                if (_playerState.value == AudioPlayerState.STATE_PLAYING) {
                    _currentSongTime.postValue(interactor.getCurrentSongTime())
                    handler.postDelayed(this, DELAY)
                }
            }
        }
    }

    init {
        _playerState.value = AudioPlayerState.STATE_DEFAULT
    }

    fun preparePlayer(track: Track?) = interactor.preparePlayer(track) { state ->
        if (state == AudioPlayerState.STATE_PREPARED) {
            handler.removeCallbacks(timerRunnable)
            _playerState.value = AudioPlayerState.STATE_PREPARED
        }
    }

    fun startPlayer() {
        interactor.startPlayer()
        _playerState.value = AudioPlayerState.STATE_PLAYING
        handler.post(timerRunnable)
    }

    fun pausePlayer() {
        interactor.pausePlayer()
        _playerState.value = AudioPlayerState.STATE_PAUSED
        handler.removeCallbacks(timerRunnable)
    }

    fun stopPlayer() {
        interactor.stopPlayer()
        _playerState.value = AudioPlayerState.STATE_STOPPED
    }

    companion object {
        private const val DELAY = 500L
        private const val DEFAULT_CURRENT_POS = 0

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.provideAudioPlayerInteractor()
                AudioPlayerViewModel(
                    interactor
                )
            }
        }
    }
}


