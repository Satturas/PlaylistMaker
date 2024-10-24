package com.example.playlist_maker_dev.settings.domain

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun switchTheme(isDarkTheme: Boolean) = repository.switchTheme(isDarkTheme)

    override fun writeToSupport() = repository.writeToSupport()

    override fun userAgreement() = repository.userAgreement()

    override fun shareTextToOtherApps() = repository.shareTextToOtherApps()

    override fun getSharedPreferencesThemeValue() = repository.getSharedPreferencesThemeValue()

    override fun editSharedPreferencesThemeValue(isDarkTheme: Boolean) =
        repository.editSharedPreferencesThemeValue(isDarkTheme)

    override fun getInitialTheme() = repository.getInitialTheme()

}