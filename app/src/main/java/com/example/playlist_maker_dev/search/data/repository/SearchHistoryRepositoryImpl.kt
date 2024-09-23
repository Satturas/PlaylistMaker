package com.example.playlist_maker_dev.search.data.repository

import android.content.SharedPreferences
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SHARED_PREFS_HISTORY = "shared_prefs_history"
private const val KEY_HISTORY = "history"

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    SearchHistoryRepository {

    /*private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_HISTORY, Context.MODE_PRIVATE)*/

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

    override fun saveTracktoHistory(param: Track) {
        val oldTrackList = getSearchHistory()
        if (oldTrackList.isNotEmpty()) {
            oldTrackList.removeIf { it.trackId == param.trackId }
            if (oldTrackList.size >= 10) {
                oldTrackList.removeLast()
            }
        }
        oldTrackList.add(0, param)
        saveSearchHistory(oldTrackList)
    }

    private fun createJsonFromTrack(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTrackFromJson(json: String): MutableList<Track> {
        val turnsType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, turnsType)
    }
}