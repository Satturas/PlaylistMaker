package com.example.playlist_maker_dev.settings.domain

interface SettingsInteractor {
    fun switchTheme(isDarkTheme: Boolean)
    fun writeToSupport()
    fun userAgreement()
    fun shareTextToOtherApps()
    fun getSharedPreferencesThemeValue(): Boolean
    fun editSharedPreferencesThemeValue(isDarkTheme: Boolean)
}