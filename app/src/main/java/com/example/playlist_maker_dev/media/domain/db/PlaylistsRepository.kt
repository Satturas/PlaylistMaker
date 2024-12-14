package com.example.playlist_maker_dev.media.domain.db

import com.example.playlist_maker_dev.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun createPlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    fun deletePlaylist(playlistId: Int)
    suspend fun insertTrack(playlistId: Int)
    suspend fun deleteTrackFromPlaylist(playlistId: Int)
}