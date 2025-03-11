package com.example.playlist_maker_dev.di

import com.example.playlist_maker_dev.media.ui.edit_playlist.EditPlaylistViewModel
import com.example.playlist_maker_dev.media.ui.media_fav_tracks.FavoriteTracksViewModel
import com.example.playlist_maker_dev.media.ui.media_root.MediaViewModel
import com.example.playlist_maker_dev.media.ui.new_playlist.CreatingPlaylistViewModel
import com.example.playlist_maker_dev.media.ui.playlist_screen.PlaylistScreenViewModel
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsViewModel
import com.example.playlist_maker_dev.player.ui.AudioPlayerViewModel
import com.example.playlist_maker_dev.search.ui.SearchViewModel
import com.example.playlist_maker_dev.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        MediaViewModel()
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatingPlaylistViewModel(get(), get())
    }

    viewModel {
        PlaylistScreenViewModel(get())
    }

    viewModel{
        EditPlaylistViewModel(get(), get())
    }

}