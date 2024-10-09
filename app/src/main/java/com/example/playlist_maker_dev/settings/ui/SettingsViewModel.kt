package com.example.playlist_maker_dev.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private val isDarkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean> get() = isDarkThemeEnabled

    init {
        isDarkThemeEnabled.value = interactor.getSharedPreferencesThemeValue()
    }

    fun writeToSupport() = interactor.writeToSupport()

    fun userAgreement() = interactor.userAgreement()

    fun shareTextToOtherApps() = interactor.shareTextToOtherApps()

    fun switchTheme(isDarkTheme: Boolean) {
        interactor.editSharedPreferencesThemeValue(isDarkTheme)
        interactor.switchTheme(isDarkTheme)
        isDarkThemeEnabled.value = isDarkTheme
    }
}