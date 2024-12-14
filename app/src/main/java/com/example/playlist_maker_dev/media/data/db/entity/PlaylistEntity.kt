package com.example.playlist_maker_dev.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String?,
    val playlistCoverUrl: String?,
    val trackIdsList: String,
    val tracksQuantity: Int,
    val addedAt: String
)