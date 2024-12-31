package com.example.playlist_maker_dev.media.data.db

import com.example.playlist_maker_dev.db.AppDatabase
import com.example.playlist_maker_dev.media.data.db.convertors.PlaylistDbConvertor
import com.example.playlist_maker_dev.media.data.db.convertors.TrackDbConvertor
import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackInPlaylistsEntity
import com.example.playlist_maker_dev.media.domain.db.PlaylistsRepository
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackDbConvertor: TrackDbConvertor
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

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        emit(playlistDbConvertor.map(playlist))
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        appDatabase.trackInPlaylistsDAO().removeTrackFromPlaylist(trackId, playlistId)
        val playlist =
            playlistDbConvertor.map(appDatabase.playlistDao().getPlaylistById(playlistId))
        val track = trackDbConvertor.map(appDatabase.trackDao().getTrackById(trackId))
        val tracks = playlist.trackIdsList.toMutableList()
        tracks.remove(trackId)
        val currentTrackLength = track.trackTimeMillis.substring(0, 2)
        appDatabase.playlistDao().deleteTrackFromPlaylist(
            playlist.id,
            tracks.joinToString(","),
            playlist.tracksQuantity - 1,
            playlist.tracksLength - currentTrackLength.toInt()
        )

        if (!isTrackExistInPlaylists(trackId)) {
            appDatabase.trackDao().deleteTrack(trackId)
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        appDatabase.trackInPlaylistsDAO()
            .insertTrack(TrackInPlaylistsEntity(0, track.trackId, playlist.id))
        val tracks = playlist.trackIdsList.toMutableList()
        tracks.add(track.trackId)
        val currentTrackLength = track.trackTimeMillis.substring(0, 2)
        appDatabase.playlistDao().insertTrackInPlaylist(
            playlist.id,
            tracks.joinToString(","),
            playlist.tracksQuantity + 1,
            playlist.tracksLength + currentTrackLength.toInt()
        )
    }

    override suspend fun getTracksOfPlaylist(playlistId: Int): Flow<List<Int>> = flow {
        val tracksList = appDatabase.trackInPlaylistsDAO().getTracksOfPlaylist(playlistId)
        emit(tracksList)
    }

    override suspend fun getTrackById(trackId: Int): Flow<Track> = flow {
        val track = appDatabase.trackDao().getTrackById(trackId)
        emit(trackDbConvertor.map(track))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private suspend fun isTrackExistInPlaylists(trackId: Int): Boolean {
        val playlists = convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylists())
        for (playlist in playlists) {
            if (playlist.trackIdsList.contains(trackId)) {
                return true
            }
        }
        return false
    }
}