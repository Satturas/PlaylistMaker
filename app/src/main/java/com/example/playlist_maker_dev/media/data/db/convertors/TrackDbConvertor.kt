package com.example.playlist_maker_dev.media.data.db.convertors

import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity
import com.example.playlist_maker_dev.search.data.dto.TrackDto
import com.example.playlist_maker_dev.search.domain.models.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis.toString(),
            track.artworkUrl100,
            track.collectionName,
            track.primaryGenreName,
            track.releaseDate,
            track.country,
            track.previewUrl,
            System.currentTimeMillis().toString()
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.primaryGenreName,
            track.releaseDate,
            track.country,
            track.previewUrl,
            true
        )
    }
}
