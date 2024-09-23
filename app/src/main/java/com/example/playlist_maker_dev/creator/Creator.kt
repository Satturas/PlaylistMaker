package com.example.playlist_maker_dev.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlist_maker_dev.main.data.MainScreenRepositoryImpl
import com.example.playlist_maker_dev.main.domain.MainScreenInteractor
import com.example.playlist_maker_dev.main.domain.MainScreenInteractorImpl
import com.example.playlist_maker_dev.main.domain.MainScreenRepository
import com.example.playlist_maker_dev.player.data.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository
import com.example.playlist_maker_dev.presentation.App
import com.example.playlist_maker_dev.presentation.App.Companion.KEY_THEME_MODE
import com.example.playlist_maker_dev.search.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_dev.search.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker_dev.search.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.settings.data.SettingsRepositoryImpl
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor
import com.example.playlist_maker_dev.settings.domain.SettingsInteractorImpl
import com.example.playlist_maker_dev.settings.domain.SettingsRepository


/*object Creator {

    private lateinit var application: App

    fun initApplication(application: App) {
        Creator.application = application
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(application))
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(TracksRepositoryImpl(RetrofitNetworkClient(application)))
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(application)
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private fun getMainScreenRepository(context: Context): MainScreenRepository {
        return MainScreenRepositoryImpl(context)
    }

    fun provideMainScreenInteractor(context: Context): MainScreenInteractor {
        return MainScreenInteractorImpl(getMainScreenRepository(context))
    }

    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(KEY_THEME_MODE, Context.MODE_PRIVATE)
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences(), context)
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

}*/