package com.example.playlist_maker_dev.media.ui.playlists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.media.domain.models.Playlist

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(playlist.coverUrl)
                .crossfade(true)
                .transformations(RoundedCornersTransformation((8.dp).value))
                .build(),
            placeholder = painterResource(R.drawable.vector_cover_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F)
        )
        Text(
            text = playlist.name,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontWeight = FontWeight(400),
            fontSize = 12.sp,
            color = colorResource(id = R.color.blackA1_white),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .sizeIn(0.dp, 16.dp, 160.dp, 16.dp)
                .padding(top = 4.dp)
        )
        Text(
            text = playlist.tracksQuantity.toString(),
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontWeight = FontWeight(400),
            fontSize = 12.sp,
            color = colorResource(id = R.color.blackA1_white),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .sizeIn(0.dp, 16.dp, 160.dp, 16.dp)
                .padding(top = 4.dp)
        )
    }
}
