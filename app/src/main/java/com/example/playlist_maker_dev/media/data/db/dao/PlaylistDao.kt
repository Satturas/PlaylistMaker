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

    @Query("UPDATE playlist_table SET playlistName = :playlistName, playlistDescription = :playlistDescription, playlistCoverUrl = :playlistCoverUrl WHERE playlistId = :playlistId")
    suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: String,
        playlistDescription: String,
        playlistCoverUrl: String
    )

    @Query("SELECT * FROM playlist_table ORDER BY addedAt DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlist_table WHERE playlistId = :playlistId")
    fun deletePlaylist(playlistId: Int)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("UPDATE playlist_table SET trackIdsList = :trackIdsList, tracksQuantity = :tracksQuantity, tracksLength = :tracksLength WHERE playlistId = :playlistId")
    suspend fun insertTrackInPlaylist(
        playlistId: Int,
        trackIdsList: String,
        tracksQuantity: Int,
        tracksLength: Int
    )

    @Query("UPDATE playlist_table SET trackIdsList = :trackIdsList, tracksQuantity = :tracksQuantity, tracksLength = :tracksLength WHERE playlistId = :playlistId")
    suspend fun deleteTrackFromPlaylist(
        playlistId: Int,
        trackIdsList: String,
        tracksQuantity: Int,
        tracksLength: Int
    )
}