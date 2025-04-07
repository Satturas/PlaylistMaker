package com.example.playlist_maker_dev.media.ui.media_root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.media.ui.media_fav_tracks.MediaFavoriteTracksScreen
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsScreen
import kotlinx.coroutines.launch

@Composable
fun MediaScreen(navController: NavController) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = pagerState.currentPage


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = colorResource(id = R.color.white_light_black1A1),
            indicator = { tabs ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabs[selectedTabIndex])
                        .height(2.dp)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_16))
                        .fillMaxSize()
                        .background(colorResource(id = R.color.blackA1_white))
                )
            },
            divider = { },
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                selectedContentColor = colorResource(id = R.color.white_light_black1A1),
                unselectedContentColor = colorResource(id = R.color.white_light_black1A1),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.favorite_tracks),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontWeight = FontWeight(500),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.blackA1_white)
                    )
                },
            )

            Tab(
                selected = selectedTabIndex == 1,
                selectedContentColor = colorResource(id = R.color.white_light_black1A1),
                unselectedContentColor = colorResource(id = R.color.white_light_black1A1),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.playlists),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontWeight = FontWeight(500),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.blackA1_white)
                    )
                },
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> MediaFavoriteTracksScreen()
                1 -> PlaylistsScreen(navController)
            }
        }
    }
}

