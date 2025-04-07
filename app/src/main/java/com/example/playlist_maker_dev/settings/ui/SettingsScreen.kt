package com.example.playlist_maker_dev.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_dev.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val isDarkThemeEnabled = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isDarkThemeEnabled.value = viewModel.getCurrentTheme()
    }
    MyScaffold(viewModel, isDarkThemeEnabled)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyScaffold(viewModel: SettingsViewModel, isDarkThemeEnabled: MutableState<Boolean>) {
    Scaffold(
        containerColor = colorResource(id = R.color.white_light_black1A1),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_text),
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
                Spacer(Modifier.height(24.dp))
                SwitchTheme(viewModel = viewModel, isDarkThemeEnabled = isDarkThemeEnabled)
                SettingsButton(
                    text = stringResource(id = R.string.share_app),
                    icon = R.drawable.share_vector
                ) {
                    viewModel.shareTextToOtherApps()
                }

                SettingsButton(
                    text = stringResource(id = R.string.support),
                    icon = R.drawable.support_vector
                ) {
                    viewModel.writeToSupport()
                }

                SettingsButton(
                    text = stringResource(id = R.string.user_agreement),
                    icon = R.drawable.forward_arrow_vector
                ) {
                    viewModel.userAgreement()
                }
            }
        })
}

@Composable
private fun SwitchTheme(viewModel: SettingsViewModel, isDarkThemeEnabled: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .height(61.dp)
            .padding(horizontal = dimensionResource(R.dimen.side_padding_16)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(id = R.string.dark_theme),
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontWeight = FontWeight(400),
            fontSize = 16.sp,
            color = colorResource(id = R.color.black_white)
        )
        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isDarkThemeEnabled.value,
            onCheckedChange = {
                isDarkThemeEnabled.value = it
                viewModel.switchTheme(isDarkThemeEnabled.value)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(R.color.track),
                uncheckedThumbColor = colorResource(R.color.track),
                checkedTrackColor = colorResource(R.color.switch_track),
                uncheckedTrackColor = colorResource(R.color.switch_track),
            )
        )

    }
}

@Composable
private fun SettingsButton(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(61.dp)
            .padding(horizontal = dimensionResource(R.dimen.side_padding_16))
            .clickable(
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontWeight = FontWeight(400),
            fontSize = 16.sp,
            color = colorResource(id = R.color.black_white)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = icon),
            colorFilter = ColorFilter.tint(colorResource(id = R.color.greyAEA_white)),
            contentDescription = null
        )
    }
}
