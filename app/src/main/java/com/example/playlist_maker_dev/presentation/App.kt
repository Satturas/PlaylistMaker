package com.example.playlist_maker_dev.presentation

import android.app.Application
import com.example.playlist_maker_dev.di.dataModule
import com.example.playlist_maker_dev.di.interactorModule
import com.example.playlist_maker_dev.di.repositoryModule
import com.example.playlist_maker_dev.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }

    companion object {
        const val KEY_THEME_MODE = "key_theme_mode"
    }
}
