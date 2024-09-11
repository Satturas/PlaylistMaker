package com.example.playlist_maker_dev.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private val isdarkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean> get() = isdarkThemeEnabled

    fun writeToSupport() = interactor.writeToSupport()

    fun userAgreement() = interactor.userAgreement()

    fun shareTextToOtherApps() = interactor.shareTextToOtherApps()

    fun switchTheme(isDarkTheme: Boolean) {
        interactor.switchTheme(isDarkTheme)
        isdarkThemeEnabled.value = isDarkTheme
    }

}