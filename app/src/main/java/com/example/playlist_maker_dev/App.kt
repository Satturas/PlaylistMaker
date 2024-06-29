package com.example.playlist_maker_dev

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(APP_THEME_MODE, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(KEY_THEME_MODE, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(KEY_THEME_MODE, darkThemeEnabled).apply()
    }

    companion object {
        const val APP_THEME_MODE = "app_theme_mode"
        const val KEY_THEME_MODE = "key_theme_mode"
    }
}
