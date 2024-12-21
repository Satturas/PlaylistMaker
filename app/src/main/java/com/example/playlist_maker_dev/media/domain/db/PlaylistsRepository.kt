package com.example.playlist_maker_dev.media.domain.db

import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun createPlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    fun deletePlaylist(playlistId: Int)
    suspend fun getPlaylistById(playlistId: Int): Flow<Playlist>
    suspend fun deleteTrackFromPlaylist(playlistId: Int)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun getTracksOfPlaylist(playlistId: Int): Flow<List<Int>>
}