package com.example.playlist_maker_dev

import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.TracksRepositoryImpl
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.api.TracksRepository
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}