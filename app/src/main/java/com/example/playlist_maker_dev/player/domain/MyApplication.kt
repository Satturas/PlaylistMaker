package com.example.playlist_maker_dev.player.domain

import android.app.Application
import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.domain.repository.TracksRepository

class MyApplication: Application() {
    fun getRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getRepository())
    }
}