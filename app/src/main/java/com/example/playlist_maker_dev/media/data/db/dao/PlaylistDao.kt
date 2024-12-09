package com.example.playlist_maker_dev.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker_dev.media.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY addedAt DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlist_table WHERE playlistId = :playlistId")
    fun deletePlaylist(playlistId: Int)

    @Query("") //TODO!!!
    suspend fun insertTrack(trackId: Int, playlistId: Int)

    @Query("DELETE FROM playlist_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)

}