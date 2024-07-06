package com.example.playlist_maker_dev

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val searchHistoryList = mutableListOf<Track>()
    var searchHistoryAdapter = TrackAdapter(mutableListOf())

    fun addTrackToHistoryList(track: Track) {
        createTrackFromJson(sharedPreferences)?.let { searchHistoryList.add(it) }

    }

    private fun createTrackFromJson(sharedPreferences: SharedPreferences): Track? {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return null
        return Gson().fromJson(json, Track::class.java)
    }


    private fun createJsonFromTrack(tracks: MutableList<Track>): String {
        return Gson().toJson(tracks)
    }

    fun writeToSharedPreferences(list: MutableList<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, createJsonFromTrack(list))
            .apply()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        searchHistoryList.clear()
        searchHistoryAdapter.tracks = searchHistoryList
        searchHistoryAdapter.notifyDataSetChanged()
    }


    companion object {
        private const val SEARCH_HISTORY_KEY = "key_for_search_history"
    }
}