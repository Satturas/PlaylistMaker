package com.example.playlist_maker_dev.settings.domain

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun switchTheme() = repository.switchTheme()

    override fun writeToSupport() = repository.writeToSupport()

    override fun userAgreement() = repository.userAgreement()

    override fun shareTextToOtherApps() = repository.shareTextToOtherApps()

}