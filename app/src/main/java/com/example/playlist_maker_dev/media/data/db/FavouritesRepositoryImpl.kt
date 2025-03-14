package com.example.playlist_maker_dev.media.data.db

import com.example.playlist_maker_dev.db.AppDatabase
import com.example.playlist_maker_dev.media.data.db.convertors.TrackDbConvertor
import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity
import com.example.playlist_maker_dev.media.domain.db.FavouritesRepository
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavouritesRepository {

    override fun addTrackToFavourites(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override fun removeTrackFromFavourites(trackId: Int) {
        appDatabase.trackDao().removeTrackFromFavourites(trackId)
    }

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavouriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun isTrackFavourite(track: Track): Flow<Boolean> = flow {
        val isFavourite = appDatabase.trackDao().isTrackFavourite(track.trackId)
        emit(isFavourite)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}