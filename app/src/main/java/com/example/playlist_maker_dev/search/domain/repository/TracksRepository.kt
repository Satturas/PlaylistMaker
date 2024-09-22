package com.example.playlist_maker_dev.search.domain.repository

import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.util.Resource

interface TracksRepository {
    fun searchTracks(term: String): Resource<List<Track>>
}