package com.example.playlist_maker_dev.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlist_maker_dev.R

@Composable
fun SearchScreen() {
    MyScaffold()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold() {

    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.search_text)) },
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(start = 14.dp),
                        painter = painterResource(id = R.drawable.search_image_vector),
                        contentDescription = null,
                    )
                    TextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        label = { Text(stringResource(id = R.string.search_text)) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1F)
                            .background(colorResource(id = R.color.search_field)),
                    )
                }
            }
            LazyColumn(
                // consume insets as scaffold doesn't do it by default
                modifier = Modifier.consumeWindowInsets(innerPadding),
                contentPadding = innerPadding
            ) {
                items(count = 100) {
                    Box(Modifier.fillMaxWidth().height(61.dp))
                }
            }
        }
    )
}


@Preview
@Composable
private fun SearhScreenPreview() {
    SearchScreen()
}

@Composable
fun SimpleTextField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = { Text("Введите текст") },
        singleLine = true,
    )
}