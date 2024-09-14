package com.example.playlist_maker_dev.main.data

import android.content.Context
import android.content.Intent
import com.example.playlist_maker_dev.main.domain.MainScreenRepository
import com.example.playlist_maker_dev.media.ui.MediaActivity
import com.example.playlist_maker_dev.search.ui.SearchActivity
import com.example.playlist_maker_dev.settings.ui.SettingsActivity

class MainScreenRepositoryImpl(private val context: Context) : MainScreenRepository {

    override fun onSearchClick() =
        context.startActivity(Intent(context, SearchActivity::class.java))

    override fun onMediaClick() = context.startActivity(Intent(context, MediaActivity::class.java))

    override fun onSettingsClick() =
        context.startActivity(Intent(context, SettingsActivity::class.java))
}