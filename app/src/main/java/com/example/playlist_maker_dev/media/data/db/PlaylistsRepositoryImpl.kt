package com.example.playlist_maker_dev.media.data.db

import android.content.Context
import android.content.Intent
import com.example.playlist_maker_dev.db.AppDatabase
import com.example.playlist_maker_dev.media.data.db.convertors.PlaylistDbConvertor
import com.example.playlist_maker_dev.media.data.db.convertors.TrackDbConvertor
import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackInPlaylistsEntity
import com.example.playlist_maker_dev.media.domain.db.PlaylistsRepository
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackDbConvertor: TrackDbConvertor,
    private val context: Context
) : PlaylistsRepository {

    override fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        playlistEntity.playlistDescription?.let {
            playlistEntity.playlistCoverUrl?.let { it1 ->
                appDatabase.playlistDao().updatePlaylist(
                    playlistEntity.playlistId, playlistEntity.playlistName,
                    it, it1
                )
            }
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        appDatabase.playlistDao().deletePlaylist(playlistId)

        val tracksList = appDatabase.trackInPlaylistsDAO().getTracksOfPlaylist(playlistId)
        for (track in tracksList) {
            if (!isTrackExistInPlaylists(track)) {
                appDatabase.trackDao().deleteTrack(track)
            }
        }
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

        if (!isTrackExistInPlaylists(trackId) && !track.isFavorite) {
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
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun getTracksOfPlaylist(playlistId: Int): Flow<List<Int>> = flow {
        val tracksList = appDatabase.trackInPlaylistsDAO().getTracksOfPlaylist(playlistId)
        emit(tracksList)
    }

    override suspend fun getTracksById(trackId: List<Int>): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracksById(trackId)
        emit(tracks.map { track -> trackDbConvertor.map(track) })
    }

    override suspend fun getTrackById(trackId: Int): Flow<Track> = flow {
        val track = appDatabase.trackDao().getTrackById(trackId)
        emit(trackDbConvertor.map(track))
    }

    override suspend fun sharePlaylistToOtherApps(playlistId: Int) {
        val playlist =
            playlistDbConvertor.map(appDatabase.playlistDao().getPlaylistById(playlistId))
        val text =
            playlist.name +
                    "\n${playlist.description}" +
                    "\n${numberOfTracks(playlist.tracksQuantity)}" +
                    "\n${
                        makeTracksInfoText(playlist)
                    }"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val shareIntent =
            Intent.createChooser(sendIntent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
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

    private fun numberOfTracks(number: Int): String {
        var tempNumber = number % 100
        if (tempNumber in 5..20) {
            return "$number треков"
        } else {
            tempNumber = number % 10
            return when (tempNumber) {
                in 1..1 -> "$number трек"
                in 2..4 -> "$number трека"
                else -> "$number треков"
            }
        }
    }

    private suspend fun makeTracksInfoText(playlist: Playlist): String {
        val text = StringBuilder()
        var number = 1

        playlist.trackIdsList.forEach { trackId ->
            val trackEntity: TrackEntity = appDatabase.trackDao().getTrackById(trackId)
            val track = trackEntity.let { trackDbConvertor.map(it) }
            text.append("$number. ${track.artistName} - ${track.trackName} (${track.trackTimeMillis})\n")
            number++
        }
        return text.toString()
    }

}



