package com.example.playlist_maker_dev.presentation

import android.app.Application
import com.example.playlist_maker_dev.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        Creator.provideSettingsInteractor(KEY_THEME_MODE)
            .switchTheme(Creator.provideSettingsRepository(KEY_THEME_MODE).getSharedPreferencesThemeValue())
        Creator.provideSharedPreferences(KEY_THEME_MODE)
    }

    companion object {
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
        const val KEY_THEME_MODE = "key_theme_mode"
    }
}
