package com.example.playlist_maker_dev.media.data.db

import com.example.playlist_maker_dev.db.AppDatabase
import com.example.playlist_maker_dev.media.data.db.convertors.PlaylistDbConvertor
import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackInPlaylistsEntity
import com.example.playlist_maker_dev.media.domain.db.PlaylistsRepository
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistsRepository {
    override fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun deletePlaylist(playlistId: Int) {
        appDatabase.playlistDao().deletePlaylist(playlistId)
    }

    override suspend fun insertTrack(playlistId: Int) {
        appDatabase.playlistDao().insertTrack(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int) {
        appDatabase.playlistDao().deleteTrackFromPlaylist(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        appDatabase.trackInPlaylistsDAO()
            .insertTrack(TrackInPlaylistsEntity(0, track.trackId, playlist.id))
        val tracks = playlist.trackIdsList.toMutableList()
        tracks.add(track.trackId)
        appDatabase.playlistDao().insertTrackInPlaylist(
            playlist.id,
            tracks.joinToString(","),
            playlist.tracksQuantity + 1
        )
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}