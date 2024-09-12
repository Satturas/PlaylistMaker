package com.example.playlist_maker_dev.player.ui

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.TracksRepository

class AudioPlayerViewModel(
    private val trackId: String,
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private var mediaPlayer = MediaPlayer()
    private lateinit var mainThreadHandler: Handler

        //private val getProductDetailsUseCase = Creator.provideGetProductDetailsUseCase()
    private val state = MutableLiveData<AudioPlayerState>()

    init {
        loadData()
    }

    private fun loadData() {
        state.value = AudioPlayerState.Loading

        tracksInteractor.loadTrackData(
            trackId = trackId,
            consumer = object : Consumer<Track> {
                override fun consume(data: ConsumerData<Track>) {
                    when (data) {
                        is ConsumerData.Error -> {
                            val error = AudioPlayerState.Error(data.message)
                            state.postValue(error)
                        }

                        is ConsumerData.Data -> {
                            val productDetailsInfo = ProductDetailsInfoMapper.map(data.value)
                            val content = AudioPlayerState.Content(productDetailsInfo)
                            state.postValue(content)
                        }
                    }
                }
            }
        )
    }

    fun getState(): LiveData<AudioPlayerState> = state

    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = (this[APPLICATION_KEY] as MyApplication).provideTracksInteractor()
                AudioPlayerViewModel(
                    trackId,
                    interactor,
                )
            }
        }
    }
}

class MyApplication : Application() {
    fun getRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getRepository())
    }
}