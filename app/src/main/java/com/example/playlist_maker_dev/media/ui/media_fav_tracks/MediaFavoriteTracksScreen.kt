package com.example.playlist_maker_dev.media.ui.media_fav_tracks

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.media.ui.media_root.MediaState
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.search.ui.SearchFragment.Companion.AUDIO_PLAYER
import com.example.playlist_maker_dev.search.ui.TrackItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun MediaFavoriteTracksScreen(viewModel: FavoriteTracksViewModel = koinViewModel()) {
    val context = LocalContext.current
    val isTrackListVisible by viewModel.isTrackListVisible.collectAsState()
    val mediaState by viewModel.mediaState.collectAsState()
    val isClickAllowed = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Spacer(Modifier.height(dimensionResource(R.dimen.side_padding_16)))
            TrackList(
                isTrackListVisible,
                mediaState = mediaState,
                isClickAllowed = isClickAllowed,
                context, scope
            )
            ShowNothingInFavorite(
                !isTrackListVisible,
                stringResource(R.string.media_library_is_empty)
            )
        }
    }
}

@Composable
fun TrackList(
    visible: Boolean,
    mediaState: MediaState,
    isClickAllowed: MutableState<Boolean>,
    context: Context,
    scope: CoroutineScope
) {

    if (!visible) return

    val tracks = (mediaState as MediaState.FavouriteTracks).favouriteTracks

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(tracks.size) { track ->
            TrackItem(
                track = tracks[track],
                onClick = {
                    if (isClickAllowed.value) {
                        isClickAllowed.value = false
                        context.startActivity(
                            Intent(
                                context,
                                AudioPlayerActivity::class.java
                            ).apply {
                                putExtra(AUDIO_PLAYER, tracks[track])
                            })
                        scope.launch {
                            delay(1000L)
                            isClickAllowed.value = true
                        }
                    }

                }
            )
        }
    }
}

@Composable
fun ShowNothingInFavorite(visible: Boolean, text: String) {

    if (!visible) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 106.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            alignment = Alignment.Center,
            painter = painterResource(R.drawable.vector_nothing_found),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.side_padding_16)),
            text = text,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontWeight = FontWeight(400),
            fontSize = 19.sp,
            color = colorResource(id = R.color.black_white),
        )
    }
}

