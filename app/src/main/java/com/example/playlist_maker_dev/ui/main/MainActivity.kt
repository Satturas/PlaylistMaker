package com.example.playlist_maker_dev.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.ui.media.MediaActivity
import com.example.playlist_maker_dev.ui.search.SearchActivity
import com.example.playlist_maker_dev.ui.settings.SettingsActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.buttonSearch)
        val mediaButton = findViewById<Button>(R.id.buttonMedia)
        val settingsButton = findViewById<Button>(R.id.buttonSettings)

        searchButton.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        mediaButton.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        settingsButton.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}