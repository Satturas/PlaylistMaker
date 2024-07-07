package com.example.playlist_maker_dev

import android.content.SharedPreferences
import com.example.playlist_maker_dev.SearchActivity.Companion.SEARCH_TRACKS_HISTORY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private fun createJsonFromTrack(tracks: MutableList<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTrackFromJson(json: String): MutableList<Track> {
        val turnsType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, turnsType)
    }

    fun writeToSharedPreferences(list: MutableList<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_TRACKS_HISTORY, createJsonFromTrack(list))
            .apply()
    }

    fun readFromSharedPreferences(): MutableList<Track> {
        val tracksList = mutableListOf<Track>()
        val tracksListString = sharedPreferences.getString(SEARCH_TRACKS_HISTORY, null)
        if (tracksListString != null) {
            tracksList.addAll(createTrackFromJson(tracksListString))
        }
        return tracksList
    }

}