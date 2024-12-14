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

    override suspend fun insertTrack(playlistId: Int) {
        playlistsRepository.insertTrack(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(playlistId)
    }
}