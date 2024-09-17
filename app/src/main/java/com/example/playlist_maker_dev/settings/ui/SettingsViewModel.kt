package com.example.playlist_maker_dev.settings.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.presentation.App.Companion.KEY_THEME_MODE
import com.example.playlist_maker_dev.search.ui.SearchViewModel
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private val isdarkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean> get() = isdarkThemeEnabled

    init {
        isdarkThemeEnabled.value = interactor.getSharedPreferencesThemeValue()
    }

    fun writeToSupport() = interactor.writeToSupport()

    fun userAgreement() = interactor.userAgreement()

    fun shareTextToOtherApps() = interactor.shareTextToOtherApps()

    fun switchTheme(isDarkTheme: Boolean) {
        interactor.editSharedPreferencesThemeValue(isDarkTheme)
        interactor.switchTheme(isDarkTheme)
        isdarkThemeEnabled.value = isDarkTheme
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(
                        Creator.provideSettingsInteractor(KEY_THEME_MODE, context),
                    )
                }
            }
    }
}