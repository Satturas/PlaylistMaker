package com.example.playlist_maker_dev

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    private fun String.shareTextToOtherApps() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, this@shareTextToOtherApps)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun writeToSupport() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, "klintsov.p@mail.ru")
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            )
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSettings)
        backFromSettingsButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareButton = findViewById<Button>(R.id.buttonShareApp)
        shareButton.setOnClickListener {
            "https://practicum.yandex.ru/android-developer/".shareTextToOtherApps()
        }

        val supportButton = findViewById<Button>(R.id.buttonWriteToSupport)
        supportButton.setOnClickListener {
            writeToSupport()
        }
    }
}