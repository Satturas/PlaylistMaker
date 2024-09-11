package com.example.playlist_maker_dev.main.ui

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.main.domain.MainScreenInteractor

class MainScreenViewModel(
    private val interactor: MainScreenInteractor
) : ViewModel() {

    fun onSearchClick() = interactor.onSearchClick()

    fun onMediaClick() = interactor.onMediaClick()

    fun onSettingsClick() = interactor.onSettingsClick()

}