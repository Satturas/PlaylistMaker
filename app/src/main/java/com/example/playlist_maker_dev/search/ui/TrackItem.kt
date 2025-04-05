package com.example.playlist_maker_dev.search.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.search.domain.models.Track


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackItem(
    track: Track,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.white_light_black1A1))
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
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
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = track.trackName,
//style = LocalTypography.current.body16Regular400,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                color = colorResource(id = R.color.black_white),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .sizeIn(0.dp, 19.dp, 250.dp, 19.dp),
            )
            Row {
                Text(
                    text = track.artistName,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterVertically)
                        .sizeIn(0.dp, 13.dp, 170.dp, 13.dp),
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontWeight = FontWeight(400),
                    fontSize = 11.sp,
                    color = colorResource(id = R.color.greyAEA_white),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_point_separator),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterVertically)

                )
                Text(
                    text = track.trackTimeMillis,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontWeight = FontWeight(400),
                    fontSize = 11.sp,
                    color = colorResource(id = R.color.greyAEA_white),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start

                )
            }

        }
        Image(
            painter = painterResource(id = R.drawable.forward_arrow_vector),
            contentDescription = null,
            alignment = Alignment.CenterEnd,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
                .padding(end = 16.dp)

        )
    }
}