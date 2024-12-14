package com.example.playlist_maker_dev.media.data.db.convertors

import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.domain.models.Playlist

class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverUrl,
            playlist.trackIdsList.joinToString(","),
            playlist.tracksQuantity,
            System.currentTimeMillis().toString()
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistCoverUrl,
            playlist.trackIdsList.split(",").map { it.trim().toInt() },
            playlist.tracksQuantity,
        )
    }
}
