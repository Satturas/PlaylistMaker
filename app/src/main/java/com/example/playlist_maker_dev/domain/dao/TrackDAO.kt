package com.example.playlist_maker_dev.domain.dao

import com.example.playlist_maker_dev.domain.models.Track

interface TrackDAO {

    fun getTracks(): List<Track>
    fun getTrackById(id: String): Track?
}