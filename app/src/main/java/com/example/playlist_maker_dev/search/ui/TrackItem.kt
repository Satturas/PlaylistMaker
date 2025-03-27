package com.example.playlist_maker_dev.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.search.domain.models.Track
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text


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
    Row(
        modifier = Modifier
            .padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .crossfade(true)
                .transformations(RoundedCornersTransformation(dimensionResource(R.dimen.round_corner_2).value))
                .build(),
            placeholder = painterResource(R.drawable.vector_cover_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
        )
        Column {
            Text(text = track.trackName)

        }
    }
}