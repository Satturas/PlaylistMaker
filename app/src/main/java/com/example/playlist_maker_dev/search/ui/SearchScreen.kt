package com.example.playlist_maker_dev.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.presentation.LocalTypography

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {
    val context = LocalContext.current
    val searchState by viewModel.searchState.observeAsState()
    MyScaffold(searchState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(state: SearchState?) {

    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.search_text),
                        //style = LocalTypography.current.h1_22Medium500
                    )
                },
                /*navigationIcon = {
                    IconButton(onClick = { /* "Open nav drawer" */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }*/
            )
        },
        content = { innerPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(horizontal = dimensionResource(id = R.dimen.side_padding_16))
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.search_field))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .padding(start = 14.dp),
                        painter = painterResource(id = R.drawable.search_image_vector),
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = stringResource(R.string.search_text),
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.search_image)
                    )
                    SimpleTextField(
                        modifier = Modifier
                            .weight(1f)
                    )
                    ClearSearchRequestButton(true)
                }
            }

            /*LazyColumn(
                // consume insets as scaffold doesn't do it by default
                modifier = Modifier.consumeWindowInsets(innerPadding),
                contentPadding = innerPadding
            ) {
                items(count = 10) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(61.dp)
                    )
                }
            }*/
            ShowTracks(true, state = state)
        }
    )
}


@Preview
@Composable
private fun SearhScreenPreview() {
    SearchScreen()
}

@Composable
fun SimpleTextField(modifier: Modifier) {
    var text by remember { mutableStateOf("") }

    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = { newValue: String ->
            text = newValue
        },
        singleLine = true,
        textStyle = TextStyle(
            color = colorResource(R.color.black),
            fontSize = 16.sp,
            textAlign = TextAlign.Start
        ),
        cursorBrush = SolidColor(colorResource(R.color.background)),
    )
}

@Composable
fun ClearSearchRequestButton(isVisible: Boolean) {
    if (!isVisible) return

    Image(
        modifier = Modifier.padding(end = 10.dp),
        painter = painterResource(id = R.drawable.delete_icon_vector),
        contentDescription = null,
    )
}

@Composable
fun ShowTracks(
    isVisible: Boolean,
    state: SearchState?
) {
    if (!isVisible) return

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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tracks.size) { item ->
            TrackItem(track = tracks[item])
        }
    }
}

