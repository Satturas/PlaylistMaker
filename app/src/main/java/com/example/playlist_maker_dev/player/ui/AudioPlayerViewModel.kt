package com.example.playlist_maker_dev.player.ui

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.core.util.Consumer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.TracksRepository
import com.example.playlist_maker_dev.player.data.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
    private val track: Track?
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    //private var mediaPlayer = MediaPlayer()
    //private lateinit var mainThreadHandler: Handler

    //private val getProductDetailsUseCase = Creator.provideGetProductDetailsUseCase()
    private val state = MutableLiveData<AudioPlayerState>()
    val playerState: LiveData<AudioPlayerState> get() = state

    init {
        state.value = AudioPlayerState.STATE_DEFAULT
    }

    val url = track?.previewUrl.toString()

    fun preparePlayer() = interactor.preparePlayer(track)


    //fun player() = interactor.loadTrackData(trackId, )

    fun getState(): LiveData<AudioPlayerState> = state

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.provideAudioPlayerInteractor()
                AudioPlayerViewModel(
                    interactor,
                    track
                )
            }
        }
    }
}


