package com.example.playlist_maker_dev.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker_dev.media.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY addedAt DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    fun deleteTrack(trackId: Int)

    @Query("SELECT COUNT(*) > 0 FROM track_table WHERE trackId = :id")
    suspend fun isTrackFavourite(id: Int): Boolean

    @Query("SELECT * FROM track_table WHERE trackId In (:trackIds)")
    suspend fun getTracksById(trackIds: List<Int>) : List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int) : TrackEntity
}