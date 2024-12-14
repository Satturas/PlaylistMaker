package com.example.playlist_maker_dev.media.ui.playlists

import com.example.playlist_maker_dev.media.domain.models.Playlist

sealed interface PlaylistsState {

    data object NoPlaylists : PlaylistsState
    data object Loading : PlaylistsState
    data class Error(val message: String) : PlaylistsState
    data class FoundPlaylistsContent(val foundPlaylists: List<Playlist>) : PlaylistsState

}