package com.example.playlist_maker_dev.media.domain.impl

import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.db.PlaylistsRepository
import com.example.playlist_maker_dev.media.domain.models.Playlist
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

    override suspend fun insertTrack(trackId: Int, playlistId: Int) {
        playlistsRepository.insertTrack(trackId, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }
}