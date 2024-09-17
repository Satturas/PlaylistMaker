package com.example.playlist_maker_dev.main.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.main.domain.MainScreenInteractor

class MainScreenViewModel(
    private val interactor: MainScreenInteractor
) : ViewModel() {

    fun onSearchClick() = interactor.onSearchClick()

    fun onMediaClick() = interactor.onMediaClick()

    fun onSettingsClick() = interactor.onSettingsClick()

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainScreenViewModel(
                        Creator.provideMainScreenInteractor(context)
                    )
                }
            }
    }
}
