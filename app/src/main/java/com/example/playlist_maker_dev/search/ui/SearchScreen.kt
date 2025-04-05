package com.example.playlist_maker_dev.search.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.search.ui.SearchFragment.Companion.AUDIO_PLAYER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {
    val context = LocalContext.current
    val searchState by viewModel.searchState.collectAsState()
    val searchInputValue = rememberSaveable { mutableStateOf("") }
    val hasFocus = remember { mutableStateOf(false) }
    val isClickAllowed = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    MyScaffold(viewModel, searchInputValue, hasFocus, searchState, isClickAllowed, context, scope)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    viewModel: SearchViewModel,
    searchInputValue: MutableState<String>,
    hasFocus: MutableState<Boolean>,
    searchState: SearchState?,
    isClickAllowed: MutableState<Boolean>,
    context: Context,
    scope: CoroutineScope
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        containerColor = colorResource(id = R.color.white_light_black1A1),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.search_text),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontWeight = FontWeight(500),
                        fontSize = 22.sp,
                        color = colorResource(id = R.color.blackA1_white),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.white_light_black1A1))
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
            ) {
                SearchField(
                    viewModel,
                    searchInputValue,
                    hasFocus
                ) {
                    keyboardController?.hide()
                    searchInputValue.value = ""
                }
                ShowTracks(
                    searchState is SearchState.FoundTracksContent || searchState is SearchState.SearchHistoryTracksContent,
                    searchState,
                    viewModel,
                    isClickAllowed, context, scope
                )
            }
        })
}

@Composable
fun SearchField(
    viewModel: SearchViewModel,
    searchInputValue: MutableState<String>,
    hasFocus: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
                .height(36.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(color = colorResource(id = R.color.search_field_background)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .padding(vertical = 10.dp),
                painter = painterResource(id = R.drawable.search_image_vector),
                colorFilter = ColorFilter.tint(colorResource(R.color.greyAEA_black1A1)),
                contentDescription = null,
            )
            SearchTextField(
                viewModel = viewModel,
                searchInputValue = searchInputValue,
                hasFocus = hasFocus,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
            )
            ClearSearchButton(isVisible = searchInputValue.value.isNotEmpty(), onClick = onClick)
        }
    }
}

@Composable
fun SearchTextField(
    viewModel: SearchViewModel,
    searchInputValue: MutableState<String>,
    hasFocus: MutableState<Boolean>,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        if (searchInputValue.value.isEmpty()) {
            Text(
                text = stringResource(R.string.search_text),
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                color = colorResource(id = R.color.greyAEA_black1A1),
                maxLines = 1
            )
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .onFocusChanged {
                    if (it.isFocused && searchInputValue.value.isEmpty()) {
                        viewModel.showHistoryOfTracks()
                    }
                },
            value = searchInputValue.value,
            onValueChange = { newValue: String ->
                searchInputValue.value = newValue

                viewModel.searchDebounce(searchInputValue.value)
                if (hasFocus.value && searchInputValue.value.isEmpty()) {
                    viewModel.showHistoryOfTracks()
                }
            },
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                color = colorResource(id = R.color.background_night_mode),
                textAlign = TextAlign.Start
            ),
            cursorBrush = SolidColor(colorResource(R.color.background)),
        )
    }

}

@Composable
fun ClearSearchButton(isVisible: Boolean, onClick: () -> Unit) {

    if (!isVisible) return

    Image(
        modifier = Modifier
            .padding(end = 10.dp)
            .clickable {
                onClick()
            },
        painter = painterResource(id = R.drawable.delete_icon_vector),
        colorFilter = ColorFilter.tint(colorResource(R.color.greyAEA_black1A1)),
        contentDescription = null
    )
}

@Composable
fun ShowTracks(
    visible: Boolean,
    state: SearchState?,
    viewModel: SearchViewModel,
    isClickAllowed: MutableState<Boolean>,
    context: Context,
    scope: CoroutineScope
) {
    if (!visible) return

    val tracks = when (state) {
        is SearchState.FoundTracksContent -> {
            state.foundTracks
        }

        is SearchState.SearchHistoryTracksContent -> {
            state.searchHistoryTracks
        }

        else -> null
    }

    if (tracks == null) return

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(tracks.size) { track ->
            TrackItem(
                track = tracks[track],
                onClick = {
                    if (isClickAllowed.value) {
                        isClickAllowed.value = false
                        viewModel.saveTrackToHistory(tracks[track])
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



