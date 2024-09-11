package com.example.playlist_maker_dev.main.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.main.data.MainScreenRepositoryImpl
import com.example.playlist_maker_dev.main.domain.MainScreenInterectorImpl

class MainScreenViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainScreenViewModel(MainScreenInterectorImpl(MainScreenRepositoryImpl(context))) as T
    }
}