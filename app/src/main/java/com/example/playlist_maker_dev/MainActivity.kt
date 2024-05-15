package com.example.playlist_maker_dev

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.buttonSearch)
        val mediaButton = findViewById<Button>(R.id.buttonMedia)
        val settingsButton = findViewById<Button>(R.id.buttonSettings)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Активно ищем!", Toast.LENGTH_SHORT).show()
            }

        }
        searchButton.setOnClickListener(imageClickListener)

        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Пока треков нет", Toast.LENGTH_SHORT).show()
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Настройки в разработке", Toast.LENGTH_SHORT)
                .show()
        }
    }
}


