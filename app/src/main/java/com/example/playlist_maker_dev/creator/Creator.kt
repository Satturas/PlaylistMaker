package com.example.playlist_maker_dev.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.repository.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_dev.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.domain.api.AudioPlayerInteractor
import com.example.playlist_maker_dev.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.impl.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.domain.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker_dev.domain.repository.TracksRepository
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.AudioPlayerRepository
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository
import com.example.playlist_maker_dev.presentation.App


object Creator {

    private lateinit var application: App

    fun initApplication(application: App) {
        Creator.application = application
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    fun provideSharedPreferences(key: String): SharedPreferences {
        return application.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    private fun provideAudioPlayerRepository(
        mediaPlayer: MediaPlayer,
        handler: Handler,
        track: Track?,
        playImageView: ImageView,
        trackTimerTextView: TextView
    ): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(
            mediaPlayer,
            handler,
            track,
            playImageView,
            trackTimerTextView
        )
    }

    fun provideAudioPlayerInteractor(
        mediaPlayer: MediaPlayer, handler: Handler, track: Track?, playImageView: ImageView,
        trackTimerTextView: TextView,
    ): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            provideAudioPlayerRepository(
                mediaPlayer,
                handler,
                track,
                playImageView,
                trackTimerTextView

            )
        )
    }
}