package com.example.playlist_maker_dev.media.ui.media_root

import com.example.playlist_maker_dev.search.domain.models.Track

sealed interface MediaState {
    data object NothingInFavourite : MediaState
    data class FavouriteTracks(val favouriteTracks: List<Track>) : MediaState
}