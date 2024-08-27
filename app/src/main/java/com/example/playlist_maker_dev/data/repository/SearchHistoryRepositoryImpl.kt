package com.example.playlist_maker_dev.data.repository

import android.content.Context
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository
import com.example.playlist_maker_dev.ui.search.SearchActivity.Companion.SEARCH_TRACKS_HISTORY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SHARED_PREFS_HISTORY = "shared_prefs_history"
private const val KEY_HISTORY = "history"

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_HISTORY, Context.MODE_PRIVATE)

    override fun saveSearchHistory(param: List<Track>) {
        sharedPreferences.edit()
            .putString(KEY_HISTORY, createJsonFromTrack(param))
            .apply()
    }

    override fun getSearchHistory(): MutableList<Track> {
        val tracksList = mutableListOf<Track>()
        val tracksListString = sharedPreferences.getString(KEY_HISTORY, null)
        if (tracksListString != null) {
            tracksList.addAll(createTrackFromJson(tracksListString))
        }
        return tracksList
    }

    private fun createJsonFromTrack(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTrackFromJson(json: String): MutableList<Track> {
        val turnsType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, turnsType)
    }
}