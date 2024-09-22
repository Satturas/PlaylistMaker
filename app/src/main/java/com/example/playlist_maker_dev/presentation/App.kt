package com.example.playlist_maker_dev.presentation

import android.app.Application
import com.example.playlist_maker_dev.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        Creator.provideSettingsInteractor(this)
            .switchTheme(Creator.provideSettingsRepository(this).getSharedPreferencesThemeValue())
    }

    companion object {
        const val KEY_THEME_MODE = "key_theme_mode"
    }
}
