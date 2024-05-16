package com.example.playlist_maker_dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSettings)

        backFromSettingsButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}