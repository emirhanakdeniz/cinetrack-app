package com.example.cinetrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CinePrimary,
    secondary = CineSecondary,
    background = CineBackgroundDark,
    surface = CineSurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = CineOnBackgroundDark,
    onSurface = CineOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = CinePrimary,
    secondary = CineSecondary,
    background = CineBackgroundLight,
    surface = CineSurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = CineOnBackgroundLight,
    onSurface = CineOnSurfaceLight
)

@Composable
fun CineTrackTheme(
    themeMode: Int, // 0 system, 1 light, 2 dark
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeMode) {
        1 -> false // light
        2 -> true // dark
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if(isDarkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
