package com.example.playlist_maker_dev.di

import android.content.Context
import com.example.playlist_maker_dev.presentation.App.Companion.KEY_THEME_MODE
import com.example.playlist_maker_dev.search.data.network.ITunesApiService
import com.example.playlist_maker_dev.search.data.network.NetworkClient
import com.example.playlist_maker_dev.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(KEY_THEME_MODE, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

}