package com.example.playlist_maker_dev.di

import android.media.MediaPlayer
import com.example.playlist_maker_dev.player.data.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerRepository
import com.example.playlist_maker_dev.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_dev.search.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.settings.data.SettingsRepositoryImpl
import com.example.playlist_maker_dev.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
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