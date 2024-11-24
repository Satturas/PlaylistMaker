package com.example.playlist_maker_dev.search.domain.api

import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(term: String): Flow<Pair<List<Track>?, String?>>
}