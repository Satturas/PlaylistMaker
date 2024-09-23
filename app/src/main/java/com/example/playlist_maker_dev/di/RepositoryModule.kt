package com.example.playlist_maker_dev.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlist_maker_dev.main.data.MainScreenRepositoryImpl
import com.example.playlist_maker_dev.main.domain.MainScreenInteractor
import com.example.playlist_maker_dev.main.domain.MainScreenInteractorImpl
import com.example.playlist_maker_dev.main.domain.MainScreenRepository
import com.example.playlist_maker_dev.player.data.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository
import com.example.playlist_maker_dev.presentation.App.Companion.KEY_THEME_MODE
import com.example.playlist_maker_dev.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_dev.search.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.settings.data.SettingsRepositoryImpl
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor
import com.example.playlist_maker_dev.settings.domain.SettingsInteractorImpl
import com.example.playlist_maker_dev.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<MainScreenRepository> {
        MainScreenRepositoryImpl(get())
    }

    single {
        androidContext()
            .getSharedPreferences(KEY_THEME_MODE, Context.MODE_PRIVATE)
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), androidContext())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

}