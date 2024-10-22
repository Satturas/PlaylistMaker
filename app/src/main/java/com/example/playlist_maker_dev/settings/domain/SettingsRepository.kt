package com.example.playlist_maker_dev.settings.domain

interface SettingsRepository {
    fun switchTheme(isDarkTheme: Boolean)
    fun writeToSupport()
    fun userAgreement()
    fun shareTextToOtherApps()
    fun getSharedPreferencesThemeValue(): Boolean
    fun editSharedPreferencesThemeValue(isDarkTheme: Boolean)
    fun getInitialTheme()
}