package com.example.playlist_maker_dev.presentation

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class CustomTypography(
    val h1_22Medium500: TextStyle,
    val body16Regular400: TextStyle,
    val body11REgular400: TextStyle,
)

val Typography = CustomTypography(
    h1_22Medium500 = TextStyle(
        fontFamily = ysFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        letterSpacing = 0.sp
    ),
    body16Regular400 = TextStyle(
        fontFamily = ysFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    body11REgular400 = TextStyle(
        fontFamily = ysFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 0.sp
    )
)