package com.example.playlist_maker_dev.di

import com.example.playlist_maker_dev.main.ui.MainScreenViewModel
import com.example.playlist_maker_dev.player.ui.AudioPlayerViewModel
import com.example.playlist_maker_dev.search.ui.SearchViewModel
import com.example.playlist_maker_dev.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        MainScreenViewModel(get())
    }

    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get())
    }

}