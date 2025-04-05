package com.example.playlist_maker_dev.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private var _isDarkThemeEnabled = MutableStateFlow<Boolean>(false)
    val isDarkThemeEnabled: StateFlow<Boolean> get() = _isDarkThemeEnabled.asStateFlow()

    init {
        _isDarkThemeEnabled.value = interactor.getSharedPreferencesThemeValue()
    }

    fun writeToSupport() = interactor.writeToSupport()

    fun userAgreement() = interactor.userAgreement()

    fun shareTextToOtherApps() = interactor.shareTextToOtherApps()

    fun getCurrentTheme() = interactor.getSharedPreferencesThemeValue()

    fun switchTheme(isDarkTheme: Boolean) {
        interactor.editSharedPreferencesThemeValue(isDarkTheme)
        interactor.switchTheme(isDarkTheme)
        _isDarkThemeEnabled.value = isDarkTheme
    }
}