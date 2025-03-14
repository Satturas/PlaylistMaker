package com.example.playlist_maker_dev.di

import android.media.MediaPlayer
import com.example.playlist_maker_dev.media.data.db.FavouritesRepositoryImpl
import com.example.playlist_maker_dev.media.data.db.PlaylistsRepositoryImpl
import com.example.playlist_maker_dev.media.data.db.convertors.PlaylistDbConvertor
import com.example.playlist_maker_dev.media.data.db.convertors.TrackDbConvertor
import com.example.playlist_maker_dev.media.domain.db.FavouritesRepository
import com.example.playlist_maker_dev.media.domain.db.PlaylistsRepository
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
        TracksRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), androidContext())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    factory { TrackDbConvertor() }

    factory { PlaylistDbConvertor() }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), get(), androidContext())
    }

}