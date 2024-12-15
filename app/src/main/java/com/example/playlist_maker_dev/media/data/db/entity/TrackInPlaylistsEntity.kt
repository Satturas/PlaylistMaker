package com.example.playlist_maker_dev.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists_table")
data class TrackInPlaylistsEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val primaryGenreName: String,
    val releaseDate: String?,
    val country: String,
    val previewUrl: String?,
    val addedAt: String
)