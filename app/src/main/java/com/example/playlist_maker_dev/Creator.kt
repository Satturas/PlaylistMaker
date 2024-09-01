package com.example.playlist_maker_dev

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.example.playlist_maker_dev.data.network.RetrofitNetworkClient
import com.example.playlist_maker_dev.data.repository.AudioPlayerRepositoryImpl
import com.example.playlist_maker_dev.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_dev.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_dev.domain.api.AudioPlayerInteractor
import com.example.playlist_maker_dev.domain.api.SearchHistoryInteractor
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.impl.AudioPlayerInteractorImpl
import com.example.playlist_maker_dev.domain.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker_dev.domain.repository.TracksRepository
import com.example.playlist_maker_dev.domain.impl.TracksInteractorImpl
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.AudioPlayerRepository
import com.example.playlist_maker_dev.domain.repository.SearchHistoryRepository


object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }


    private fun provideAudioPlayerRepository(
        mediaPlayer: MediaPlayer,
        handler: Handler,
        track: Track?,
        playImageView: ImageView,
        trackTimerTextView: TextView
    ): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(
            mediaPlayer,
            handler,
            track,
            playImageView,
            trackTimerTextView
        )
    }

    fun provideAudioPlayerInteractor(
        mediaPlayer: MediaPlayer, handler: Handler, track: Track?, playImageView: ImageView,
        trackTimerTextView: TextView,
    ): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            provideAudioPlayerRepository(
                mediaPlayer,
                handler,
                track,
                playImageView,
                trackTimerTextView

            )
        )
    }
}