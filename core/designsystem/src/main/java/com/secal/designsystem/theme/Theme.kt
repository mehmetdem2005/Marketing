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
    tertiary = SecalPalette.Amber40,
    onTertiary = SecalPalette.White,
    tertiaryContainer = SecalPalette.Amber90,
    onTertiaryContainer = SecalPalette.Amber10,
    background = SecalPalette.Background,
    onBackground = SecalPalette.Neutral10,
    surface = SecalPalette.Surface,
    onSurface = SecalPalette.Neutral10,
    surfaceVariant = SecalPalette.SurfaceVariant,
    onSurfaceVariant = SecalPalette.OnSurfaceVariant,
    surfaceContainerLowest = SecalPalette.White,
    surfaceContainerLow = SecalPalette.SurfaceContainerLow,
    surfaceContainer = SecalPalette.SurfaceContainer,
    surfaceContainerHigh = SecalPalette.SurfaceContainerHigh,
    outline = SecalPalette.Outline,
    outlineVariant = SecalPalette.OutlineVariant,
    error = SecalPalette.Error40,
    onError = SecalPalette.White,
    errorContainer = SecalPalette.ErrorContainer,
    onErrorContainer = SecalPalette.OnErrorContainer,
    scrim = SecalPalette.Scrim,
)

private val DarkColors = darkColorScheme(
    primary = SecalPalette.Green80,
    onPrimary = SecalPalette.Green20,
    primaryContainer = SecalPalette.Green30,
    onPrimaryContainer = SecalPalette.Green90,
    secondary = SecalPalette.Wheat80,
    onSecondary = SecalPalette.Wheat20,
    secondaryContainer = SecalPalette.Wheat40,
    onSecondaryContainer = SecalPalette.Wheat90,
    tertiary = SecalPalette.Amber90,
    onTertiary = SecalPalette.Amber10,
    background = SecalPalette.SurfaceDark,
    onBackground = SecalPalette.OnSurfaceDark,
    surface = SecalPalette.SurfaceDark,
    onSurface = SecalPalette.OnSurfaceDark,
    surfaceVariant = SecalPalette.Neutral30,
    onSurfaceVariant = SecalPalette.OutlineVariant,
    surfaceContainer = SecalPalette.SurfaceContainerDark,
    outline = SecalPalette.Neutral50,
    error = SecalPalette.Error80,
    onError = SecalPalette.Neutral10,
)

/**
 * SeçAl uygulama teması. Tüm ekranlar bunun içinde sarmalanır.
 * Marka renkleri (dinamik renk değil — tutarlı kimlik) + yuvarlatılmış şekiller.
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
            shapes = SecalShapes,
            content = content,
        )
    }
}
