package com.example.playlist_maker_dev.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker_dev.media.data.db.dao.PlaylistDao
import com.example.playlist_maker_dev.media.data.db.dao.TrackDao
import com.example.playlist_maker_dev.media.data.db.dao.TrackInPlaylistsDAO
import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackInPlaylistsEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistsEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistsDAO(): TrackInPlaylistsDAO
}