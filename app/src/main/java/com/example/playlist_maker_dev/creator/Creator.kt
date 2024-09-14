package com.example.playlist_maker_dev.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_dev.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.domain.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository
import com.example.playlist_maker_dev.domain.repository.TracksRepository
import com.example.playlist_maker_dev.player.data.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.presentation.App
import com.example.playlist_maker_dev.settings.data.SettingsRepositoryImpl
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor
import com.example.playlist_maker_dev.settings.domain.SettingsInteractorImpl
import com.example.playlist_maker_dev.settings.domain.SettingsRepository


object Creator {

    private lateinit var application: App

    private lateinit var sharedPreferences: SharedPreferences

    fun initApplication(application: App) {
        Creator.application = application
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(TracksRepositoryImpl(RetrofitNetworkClient(context)))
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

    fun initialize(sharedPreferences: SharedPreferences) {
        Creator.sharedPreferences = sharedPreferences
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context))
    }

    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(

        )
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            provideAudioPlayerRepository(
            )
        )
    }

}