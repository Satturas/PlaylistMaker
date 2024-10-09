package com.example.playlist_maker_dev.media.ui

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_dev.media.domain.MediaInteractor

class MediaViewModel(
    private val interactor: MediaInteractor
) : ViewModel() {}
