package com.example.playlist_maker_dev.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.playlist_maker_dev.search.domain.models.Track

@Composable
fun TrackItem(
    track: Track
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
    ) {
        TrackCover(track)

    }

}

@Preview
@Composable
private fun TrackItemPreview() {
    val testTrack = Track(
        trackId = 15,
        trackName = "Компромисс",
        artistName = "Би-2",
        "4220",
        "https://geometria.ru/upload/geometria/exclusive/597423/30598267.jpg",
        "metropolitan orchestra",
        "rock",
        "2017",
        "USA",
        "https://geometria.ru/upload/geometria/exclusive/597423/30598267.jpg",
        true
    )
    TrackItem(testTrack)
}

@Composable
fun TrackCover(track: Track) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(16.dp)
    ) {
        AsyncImage(
            model = track.artworkUrl100,
            contentDescription = "Track cover"
        )
    }
}