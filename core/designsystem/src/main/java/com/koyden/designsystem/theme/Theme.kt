package com.koyden.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColors = lightColorScheme(
    primary = KoydenPalette.Green40,
    onPrimary = KoydenPalette.White,
    primaryContainer = KoydenPalette.Green90,
    onPrimaryContainer = KoydenPalette.Green10,
    secondary = KoydenPalette.Wheat40,
    onSecondary = KoydenPalette.White,
    secondaryContainer = KoydenPalette.Wheat90,
    onSecondaryContainer = KoydenPalette.Wheat10,
    background = KoydenPalette.Surface,
    onBackground = KoydenPalette.Neutral10,
    surface = KoydenPalette.Surface,
    onSurface = KoydenPalette.Neutral10,
    error = KoydenPalette.Error40,
    onError = KoydenPalette.White,
)

private val DarkColors = darkColorScheme(
    primary = KoydenPalette.Green80,
    onPrimary = KoydenPalette.Green20,
    primaryContainer = KoydenPalette.Green40,
    onPrimaryContainer = KoydenPalette.Green90,
    secondary = KoydenPalette.Wheat80,
    onSecondary = KoydenPalette.Wheat20,
    secondaryContainer = KoydenPalette.Wheat40,
    onSecondaryContainer = KoydenPalette.Wheat90,
    background = KoydenPalette.SurfaceDark,
    onBackground = KoydenPalette.Neutral90,
    surface = KoydenPalette.SurfaceDark,
    onSurface = KoydenPalette.Neutral90,
    error = KoydenPalette.Error80,
    onError = KoydenPalette.Neutral10,
)

/**
 * Köyden uygulama teması. Tüm ekranlar bunun içinde sarmalanır.
 * Dinamik renk yerine marka renkleri kullanılır (tutarlı kimlik).
 */
@Composable
fun KoydenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = KoydenTypography,
            content = content,
        )
    }
}
