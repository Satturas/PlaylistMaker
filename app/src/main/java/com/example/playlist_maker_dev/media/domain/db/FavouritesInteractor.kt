package com.example.playlist_maker_dev.media.domain.db

import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {
    fun addTrackToFavourites(track: Track)
    fun removeTrackFromFavourites(trackId: Int)
    fun getFavouriteTracks(): Flow<List<Track>>
    fun isTrackFavourite(track: Track): Flow<Boolean>
}