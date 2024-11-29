package com.example.playlist_maker_dev.search.data.repository

import android.content.SharedPreferences
import com.example.playlist_maker_dev.db.AppDatabase
import com.example.playlist_maker_dev.media.data.db.convertors.TrackDbConvertor
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val KEY_HISTORY = "history"

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) :
    SearchHistoryRepository {

    private var tracksList = mutableListOf<Track>()

    override fun saveSearchHistory(param: List<Track>) {
        sharedPreferences.edit()
            .putString(KEY_HISTORY, createJsonFromTrack(param))
            .apply()
    }

    override fun getSearchHistory(): Flow<MutableList<Track>> = flow {
        val data = mutableListOf<Track>()
        val tracksListString = sharedPreferences.getString(KEY_HISTORY, null)
        if (tracksListString != null) {
            data.addAll(createTrackFromJson(tracksListString))
        }
        data.map { it.isFavorite = appDatabase.trackDao().isTrackFavourite(it.trackId) }
        tracksList.clear()
        tracksList.addAll(data)
        emit(data)
    }

    override fun saveTrackToHistory(param: Track) {
        val oldTrackList = tracksList
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