package com.example.playlist_maker_dev.search.data.dto

data class TracksSearchResponse(
    val searchType: String,
    val term: String,
    val results: List<TrackDto>
) : Response()
