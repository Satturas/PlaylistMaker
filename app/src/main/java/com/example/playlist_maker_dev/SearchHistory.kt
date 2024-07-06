package com.example.playlist_maker_dev

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val searchHistoryList = mutableListOf<Track>()

    private fun addTrackToHistoryList(track: Track) {
        createTrackFromJson(sharedPreferences)?.let { searchHistoryList.add(it) }

    }

    private fun createTrackFromJson(sharedPreferences: SharedPreferences): Track? {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return null
        return Gson().fromJson(json, Track::class.java)
    }


    private fun createJsonFromTrack(track: Track): String {
        return Gson().toJson(track)
    }

    fun writeToSharedPreferences(){
        val json = Gson().toJson(searchHistoryList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }


    companion object {
        private const val SEARCH_HISTORY_KEY = "key_for_search_history"
    }
}