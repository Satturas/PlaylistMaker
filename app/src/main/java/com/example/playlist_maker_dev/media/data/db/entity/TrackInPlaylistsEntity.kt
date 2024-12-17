package com.example.playlist_maker_dev.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists_table")
data class TrackInPlaylistsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trackId: Int,
    val playlistId: Int
)