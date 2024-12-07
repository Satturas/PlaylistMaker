package com.example.playlist_maker_dev.di


import com.example.playlist_maker_dev.media.domain.db.FavouritesInteractor
import com.example.playlist_maker_dev.media.domain.impl.FavouritesInteractorImpl
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractor
import com.example.playlist_maker_dev.player.domain.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.search.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker_dev.search.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor
import com.example.playlist_maker_dev.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }
}