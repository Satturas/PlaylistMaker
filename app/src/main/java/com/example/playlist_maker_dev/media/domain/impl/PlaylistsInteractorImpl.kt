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

    override fun deletePlaylist(playlistId: Int) {
        playlistsRepository.deletePlaylist(playlistId)
    }

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(playlistId)
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
}