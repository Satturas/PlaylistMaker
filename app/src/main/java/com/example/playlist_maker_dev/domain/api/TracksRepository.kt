package com.example.playlist_maker_dev.domain.api

import com.example.playlist_maker_dev.domain.models.Track

interface TracksRepository {
    fun searchTracks(term: String): List<Track>
}