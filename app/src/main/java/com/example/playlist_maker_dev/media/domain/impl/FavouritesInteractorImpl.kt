package com.example.playlist_maker_dev.media.domain.impl

import com.example.playlist_maker_dev.media.domain.db.FavouritesInteractor
import com.example.playlist_maker_dev.media.domain.db.FavouritesRepository
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl(private val favouritesRepository: FavouritesRepository) :
    FavouritesInteractor {
    override fun addTrackToFavourites(track: Track) {
        favouritesRepository.addTrackToFavourites(track)
    }

    override fun removeTrackFromFavourites(trackId: Int) {
        favouritesRepository.removeTrackFromFavourites(trackId)
    }

    override fun getFavouriteTracks(): Flow<List<Track>> {
        return favouritesRepository.getFavouriteTracks()
    }

    override fun isTrackFavourite(track: Track): Flow<Boolean> {
        return favouritesRepository.isTrackFavourite(track)
    }
}