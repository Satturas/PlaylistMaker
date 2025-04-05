package com.example.playlist_maker_dev.media.ui.playlists

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.core.os.bundleOf
import androidx.fragment.compose.AndroidFragment
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.media.ui.playlist_screen.PlaylistScreenFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun PlaylistsScreen(viewModel: PlaylistsViewModel = koinViewModel()) {

    val isPlaylistsListVisible by viewModel.isPlaylistsListVisible.collectAsState()
    val playlistsState by viewModel.playlistsState.collectAsState()
    val isClickAllowed = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(dimensionResource(R.dimen.side_padding_24)))

            AddingPlaylistButton {}

            ShowPlaylists(
                isPlaylistsListVisible,
                playlistsState = playlistsState,
                isClickAllowed = isClickAllowed,
                scope, context
            )
            ShowNoPlaylists(
                !isPlaylistsListVisible
            )
        }
    }
}

@Composable
fun AddingPlaylistButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.blackA1_white)
        ),
    ) {
        Text(
            text = stringResource(id = R.string.new_playlist),
            color = colorResource(id = R.color.white_light_black1A1)
        )
    }
}

@Composable
fun ShowPlaylists(
    visible: Boolean,
    playlistsState: PlaylistsState,
    isClickAllowed: MutableState<Boolean>,
    scope: CoroutineScope,
    context: Context
) {

    if (!visible) return

    val playlists = (playlistsState as PlaylistsState.FoundPlaylistsContent).foundPlaylists

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(playlists.size) { playlist ->
            PlaylistItem(
                playlist = playlists[playlist],
                onClick = {
                    if (isClickAllowed.value) {
                        isClickAllowed.value = false
                        val args =
                            bundleOf(PlaylistsFragment.PLAYLIST_ID_KEY to playlists[playlist].id)
                        AndroidFragment(
                            clazz = PlaylistScreenFragment::class.java,
                            arguments = args
                        )
                        LaunchedEffect(Unit) {
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
fun ShowNoPlaylists(visible: Boolean) {

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
            text = stringResource(id = R.string.no_playlists_yet),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontWeight = FontWeight(400),
            fontSize = 19.sp,
            color = colorResource(id = R.color.black_white),
        )
        Text(
            text = stringResource(id = R.string.no_playlists_yet2),
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            fontWeight = FontWeight(400),
            fontSize = 19.sp,
            color = colorResource(id = R.color.black_white),
        )
    }
}



