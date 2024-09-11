package com.example.playlist_maker_dev.settings.domain

interface SettingsRepository {
    fun switchTheme()
    fun writeToSupport()
    fun userAgreement()
    fun shareTextToOtherApps()
}