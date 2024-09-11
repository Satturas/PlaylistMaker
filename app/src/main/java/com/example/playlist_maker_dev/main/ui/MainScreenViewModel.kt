package com.example.playlist_maker_dev.main.ui

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.savedstate.SavedStateRegistryOwner
import com.example.playlist_maker_dev.main.data.MainScreenRepositoryImpl
import com.example.playlist_maker_dev.main.domain.MainScreenInterector
import com.example.playlist_maker_dev.main.domain.MainScreenInterectorImpl

class MainScreenViewModel(
    private val interactor: MainScreenInterector
) : ViewModel() {

    fun onSearchClick() = interactor.onSearchClick()

    fun onMediaClick() = interactor.onMediaClick()

    fun onSettingsClick() = interactor.onSettingsClick()

}