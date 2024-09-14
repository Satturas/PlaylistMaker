package com.example.playlist_maker_dev.search.data.dto

import com.example.playlist_maker_dev.search.domain.models.Track

data class TracksSearchResponse(
    val searchType: String,
    val term: String,
    val results: List<TrackDto>
) : Response()
