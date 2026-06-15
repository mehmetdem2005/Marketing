package com.secal.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 8pt grid boşluk token'ları. Compose ağacında `LocalSpacing` ile erişilir;
 * ekranlarda sabit `dp` yerine bu token'lar kullanılır (tutarlı ritim).
 */
data class Spacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
