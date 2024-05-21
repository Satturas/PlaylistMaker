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
            putExtra(Intent.EXTRA_EMAIL, getString(R.string.user_email))
            putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.support_email_subject)
            )
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.support_email_text)
            )
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun userAgreement() = startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(getString(R.string.user_agreement_link))
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSettings)
        backFromSettingsButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareButton = findViewById<Button>(R.id.buttonShareApp)
        shareButton.setOnClickListener {
            getString(R.string.share_text).shareTextToOtherApps()
        }

        val supportButton = findViewById<Button>(R.id.buttonWriteToSupport)
        supportButton.setOnClickListener {
            writeToSupport()
        }

        val agreementButton = findViewById<Button>(R.id.buttonUserAgreement)
        agreementButton.setOnClickListener {
            userAgreement()
        }
    }
}