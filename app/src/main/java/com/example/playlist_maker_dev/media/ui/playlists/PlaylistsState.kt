package com.example.playlist_maker_dev.media.ui.playlists

import com.example.playlist_maker_dev.media.domain.models.Playlist

sealed interface PlaylistsState {

    data object NoPlaylists : PlaylistsState
    data class FoundPlaylistsContent(val foundPlaylists: List<Playlist>) : PlaylistsState

}