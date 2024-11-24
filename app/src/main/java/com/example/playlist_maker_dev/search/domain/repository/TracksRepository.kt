package com.example.playlist_maker_dev.search.domain.repository

import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(term: String): Flow<Resource<List<Track>>>
}