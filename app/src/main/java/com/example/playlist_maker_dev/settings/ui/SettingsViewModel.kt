package com.example.playlist_maker_dev.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    fun writeToSupport() = interactor.writeToSupport()

    fun userAgreement() = interactor.userAgreement()

    fun shareTextToOtherApps() = interactor.shareTextToOtherApps()

}