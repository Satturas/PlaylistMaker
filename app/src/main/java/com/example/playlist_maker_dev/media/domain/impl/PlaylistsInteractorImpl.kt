package com.example.playlist_maker_dev.media.domain.impl

import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.db.PlaylistsRepository
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {
    override fun createPlaylist(playlist: Playlist) {
        playlistsRepository.createPlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistsRepository.deletePlaylist(playlistId)
    }

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistsRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun getTracksOfPlaylist(playlistId: Int): Flow<List<Int>> {
        return playlistsRepository.getTracksOfPlaylist(playlistId)
    }

    override suspend fun getTrackById(trackId: Int): Flow<Track> {
        return playlistsRepository.getTrackById(trackId)
    }

    override suspend fun sharePlaylistToOtherApps(playlistId: Int) {
        playlistsRepository.sharePlaylistToOtherApps(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsRepository.updatePlaylist(playlist)
    }

    override suspend fun getTracksById(trackId: List<Int>): Flow<List<Track>> {
        return playlistsRepository.getTracksById(trackId)
    }
}