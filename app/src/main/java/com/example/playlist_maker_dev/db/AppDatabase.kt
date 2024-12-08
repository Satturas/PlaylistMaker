package com.example.playlist_maker_dev.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker_dev.media.data.db.dao.PlaylistDao
import com.example.playlist_maker_dev.media.data.db.dao.TrackDao
import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity
import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}