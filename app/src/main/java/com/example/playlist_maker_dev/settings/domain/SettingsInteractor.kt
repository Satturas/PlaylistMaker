package com.example.playlist_maker_dev.settings.domain

interface SettingsInteractor {
    fun switchTheme()
    fun writeToSupport()
    fun userAgreement()
    fun shareTextToOtherApps()
}