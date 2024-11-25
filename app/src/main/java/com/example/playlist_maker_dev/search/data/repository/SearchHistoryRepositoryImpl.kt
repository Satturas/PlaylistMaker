package com.example.playlist_maker_dev.search.data.repository

import android.content.SharedPreferences
import com.example.playlist_maker_dev.db.AppDatabase
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val KEY_HISTORY = "history"

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase
) :
    SearchHistoryRepository {

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
        tracksList.map { it.isFavorite = appDatabase.trackDao().getTracksId().contains(it.trackId) }
        return tracksList
    }

    override fun saveTrackToHistory(param: Track) {
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