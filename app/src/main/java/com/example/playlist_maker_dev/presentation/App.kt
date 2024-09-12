package com.example.playlist_maker_dev.presentation

import android.app.Application
import com.example.playlist_maker_dev.Creator

class App : Application() {
    //var darkTheme = false
    //private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        Creator.provideSettingsInteractor(this)
            .switchTheme(Creator.provideSettingsRepository(this).getSharedPreferencesThemeValue())
        Creator.initialize(Creator.provideSharedPreferences(KEY_THEME_MODE))

        /*sharedPreferences = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        if (!sharedPreferences.contains(KEY_THEME_MODE)) {
            sharedPreferences.edit().putBoolean(KEY_THEME_MODE, isUsingNightModeResources()).apply()
        }
        darkTheme = sharedPreferences.getBoolean(KEY_THEME_MODE, false)*/
    }

    /*fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(KEY_THEME_MODE, darkThemeEnabled).apply()
    }*/

    /*private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }*/

    companion object {
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
        const val KEY_THEME_MODE = "key_theme_mode"
    }
}
