package com.example.playlist_maker_dev.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlist_maker_dev.media.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(trackInPlaylistsEntity: TrackInPlaylistsEntity)
}