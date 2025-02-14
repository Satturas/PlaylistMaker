package com.example.playlist_maker_dev.media.ui.edit_playlist

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_dev.media.domain.db.PlaylistsInteractor
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.new_playlist.CreatingPlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class EditPlaylistViewModel(
    application: Application, playlistsInteractor: PlaylistsInteractor
) : CreatingPlaylistViewModel(application, playlistsInteractor) {

    private var currentFilePath = ""
    private var currentFileUri = ""

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    override fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(
                application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlist_covers_album"
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        currentFilePath = System.currentTimeMillis().toString()
        val file = File(filePath, currentFilePath)
        val inputStream = application.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        currentFileUri = file.toURI().toString()
    }

    fun updatePlaylist(
        id: Int,
        name: String,
        description: String?,
        url: String,
        tracksQuantity: Int,
        tracksLength: Int
    ) {

        val currentUri = if (url == "-1") currentFileUri else url
        val playlist = Playlist(
            id, name, description, currentUri,
            mutableListOf(), tracksQuantity, tracksLength
        )

        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistById(playlistId).collect { playlist ->
                _playlist.postValue(playlist)
            }
        }
    }
}