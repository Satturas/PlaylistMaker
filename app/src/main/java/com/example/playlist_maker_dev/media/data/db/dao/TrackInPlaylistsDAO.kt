package com.example.playlist_maker_dev.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker_dev.media.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(trackInPlaylistsEntity: TrackInPlaylistsEntity)

    @Query("SELECT trackId FROM tracks_in_playlists_table WHERE playlistId = :playlistId ORDER BY playlistId DESC")
    suspend fun getTracksOfPlaylist(playlistId: Int): List<Int>
}