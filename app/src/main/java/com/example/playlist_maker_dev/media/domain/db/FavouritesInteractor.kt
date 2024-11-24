package com.example.playlist_maker_dev.media.domain.db

import com.example.playlist_maker_dev.search.data.dto.TrackDto
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {
    fun addTrackToFavourites(track: TrackDto)
    fun removeTrackFromFavourites(trackId: String)
    fun getFavouriteTracks(): Flow<List<Track>>
}