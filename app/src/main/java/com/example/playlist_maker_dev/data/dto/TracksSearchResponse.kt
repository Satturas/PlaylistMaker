package com.example.playlist_maker_dev.data.dto

import com.example.playlist_maker_dev.domain.models.Track

data class TracksSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
) : Response()
