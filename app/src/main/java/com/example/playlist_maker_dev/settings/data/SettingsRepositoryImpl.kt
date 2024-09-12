package com.example.playlist_maker_dev.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.Creator
import com.example.playlist_maker_dev.presentation.App.Companion.KEY_THEME_MODE
import com.example.playlist_maker_dev.presentation.App.Companion.PLAYLISTMAKER_PREFERENCES
import com.example.playlist_maker_dev.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    override fun switchTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        editSharedPreferencesThemeValue(isDarkTheme)
    }

    override fun writeToSupport() {
        Intent().apply {
            action = Intent.ACTION_SEND
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, context.getString(R.string.user_email))
            putExtra(
                Intent.EXTRA_SUBJECT,
                context.getString(R.string.support_email_subject)
            )
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.support_email_text)
            )
            val shareIntent = Intent.createChooser(this, null)
            context.startActivity(shareIntent)
        }
    }

    override fun userAgreement() = context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(context.getString(R.string.user_agreement_link))
        )
    )

    override fun shareTextToOtherApps() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    override fun getSharedPreferencesThemeValue(): Boolean =
        Creator.provideSharedPreferences(PLAYLISTMAKER_PREFERENCES)
            .getBoolean(KEY_THEME_MODE, false)

    override fun editSharedPreferencesThemeValue(isDarkTheme: Boolean) =
        Creator.provideSharedPreferences(PLAYLISTMAKER_PREFERENCES).edit()
            .putBoolean(KEY_THEME_MODE, isDarkTheme)
            .apply()

}

