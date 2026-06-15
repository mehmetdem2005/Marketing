package com.secal.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColors = lightColorScheme(
    primary = SecalPalette.Green40,
    onPrimary = SecalPalette.White,
    primaryContainer = SecalPalette.Green90,
    onPrimaryContainer = SecalPalette.Green10,
    secondary = SecalPalette.Wheat40,
    onSecondary = SecalPalette.White,
    secondaryContainer = SecalPalette.Wheat90,
    onSecondaryContainer = SecalPalette.Wheat10,
    background = SecalPalette.Surface,
    onBackground = SecalPalette.Neutral10,
    surface = SecalPalette.Surface,
    onSurface = SecalPalette.Neutral10,
    error = SecalPalette.Error40,
    onError = SecalPalette.White,
)

private val DarkColors = darkColorScheme(
    primary = SecalPalette.Green80,
    onPrimary = SecalPalette.Green20,
    primaryContainer = SecalPalette.Green40,
    onPrimaryContainer = SecalPalette.Green90,
    secondary = SecalPalette.Wheat80,
    onSecondary = SecalPalette.Wheat20,
    secondaryContainer = SecalPalette.Wheat40,
    onSecondaryContainer = SecalPalette.Wheat90,
    background = SecalPalette.SurfaceDark,
    onBackground = SecalPalette.Neutral90,
    surface = SecalPalette.SurfaceDark,
    onSurface = SecalPalette.Neutral90,
    error = SecalPalette.Error80,
    onError = SecalPalette.Neutral10,
)

/**
 * SeçAl uygulama teması. Tüm ekranlar bunun içinde sarmalanır.
 * Dinamik renk yerine marka renkleri kullanılır (tutarlı kimlik).
 */
@Composable
fun SecalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SecalTypography,
            content = content,
        )
    }
}
