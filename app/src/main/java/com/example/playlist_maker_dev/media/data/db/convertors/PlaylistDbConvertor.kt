package com.example.playlist_maker_dev.media.data.db.convertors

import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.search.domain.models.Track

class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverUrl,
            playlist.trackIdsList.joinToString(","),
            playlist.tracksQuantity
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
