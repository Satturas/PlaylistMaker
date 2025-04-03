package com.example.playlist_maker_dev.presentation

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = YpBlack,
    secondary = YpGrey,
    tertiary = YpWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = YpWhite,
    secondary = YpBlack,
    tertiary = YpBlack
)

data class CustomColors(
    val topAppBar: TopAppBarColors,
    val editText: EditTextColors,
    val lazyColumn: LazyColumnColors,
    val button: ButtonColors
)

data class TopAppBarColors(
    val primaryColors: SimpleViewStateColors
)

data class EditTextColors(
    val primaryColors: SimpleViewStateColors
)

data class LazyColumnColors(
    val primaryColors: LazyColumnStateColors
)

data class ButtonColors(
    val ordinaryButtonColors: ButtonStateColors,
    val settingsScreenButtonColors: ButtonStateColors
)

data class SimpleViewStateColors(
    val background: Color,
    val textColor: Color
)

data class LazyColumnStateColors(
    val background: Color,
    val primaryTextColor: Color,
    val secondaryTextColor: Color
)

data class ButtonStateColors(
    val background: Color,
    val primaryTextColor: Color,
    val secondaryTextColor: Color
)


private val LightCustomColors = CustomColors(
    topAppBar= TopAppBarColors(
        primaryColors = SimpleViewStateColors(
            background = TopAppBarBackgroundLight,
            textColor = TopAppBarTextLight
        )
    ),
    editText = EditTextColors(
        primaryColors = SimpleViewStateColors(
            background = EditTextBackgroundLight,
            textColor = EditTextTextLight
        )
    ),
    lazyColumn = LazyColumnColors(
        LazyColumnStateColors(
            background = EditTextBackgroundLight,
            primaryTextColor = LazyColumPrimaryTextLight,
            secondaryTextColor = LazyColumSecondaryTextLight
        )
    ),
    button = ButtonColors(
        ordinaryButtonColors = ButtonStateColors(
            background = OrdinaryButtonBackgroundLight,
            primaryTextColor = OrdinaryButtonPrimaryTextLight,
            secondaryTextColor = OrdinaryButtonSecondaryTextLight
        ),
        settingsScreenButtonColors = ButtonStateColors(
            background = SettingsScreenButtonBackgroundLight,
            primaryTextColor = SettingsScreenButtonPrimaryTextLight,
            secondaryTextColor = SettingsScreenButtonSecondaryTextLight
        )
    )
)

private val DarkCustomColors = CustomColors(
    topAppBar = TopAppBarColors(
        primaryColors = SimpleViewStateColors(
            background = TopAppBarBackgroundDark,
            textColor = TopAppBarTextDark
        )
    ),
    editText = EditTextColors(
        primaryColors = SimpleViewStateColors(
            background = EditTextBackgroundDark,
            textColor = EditTextTextDark
        )
    ),
    lazyColumn = LazyColumnColors(
        LazyColumnStateColors(
            background = EditTextBackgroundDark,
            primaryTextColor = LazyColumPrimaryTextDark,
            secondaryTextColor = LazyColumSecondaryTextDark
        )
    ),
    button = ButtonColors(
        ordinaryButtonColors = ButtonStateColors(
            background = OrdinaryButtonBackgroundDark,
            primaryTextColor = OrdinaryButtonPrimaryTextDark,
            secondaryTextColor = OrdinaryButtonSecondaryTextDark
        ),
        settingsScreenButtonColors = ButtonStateColors(
            background = SettingsScreenButtonBackgroundDark,
            primaryTextColor = SettingsScreenButtonPrimaryTextDark,
            secondaryTextColor = SettingsScreenButtonSecondaryTextDark
        )
    )
)


val LocalCustomColors = staticCompositionLocalOf<CustomColors> {
    error("No CustomColors provided")
}

val LocalTypography = staticCompositionLocalOf<CustomTypography> {
    error("No Typography provided")
}

@Composable
fun ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalTypography provides Typography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}