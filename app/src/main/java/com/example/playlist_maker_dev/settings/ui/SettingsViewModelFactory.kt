package com.example.playlist_maker_dev.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.settings.data.SettingsRepositoryImpl
import com.example.playlist_maker_dev.settings.domain.SettingsInteractorImpl

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(SettingsInteractorImpl(SettingsRepositoryImpl(context))) as T
    }
}