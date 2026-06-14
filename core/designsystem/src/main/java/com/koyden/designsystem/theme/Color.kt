package com.koyden.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Köyden marka renk token'ları (tek kaynak — inline renk YASAK).
 * Doğal/köy teması: orman yeşili + sıcak toprak/buğday tonları.
 * Kontrast WCAG 2.2 AA hedefi (≥4.5:1 metin) Faz 1'de token bazında doğrulanır.
 */
internal object KoydenPalette {
    // Birincil — orman yeşili
    val Green10 = Color(0xFF00210B)
    val Green20 = Color(0xFF00390F)
    val Green40 = Color(0xFF2F6B3C)
    val Green80 = Color(0xFF9BD4A4)
    val Green90 = Color(0xFFB7F0BF)

    // İkincil — buğday/toprak
    val Wheat10 = Color(0xFF271900)
    val Wheat20 = Color(0xFF402D00)
    val Wheat40 = Color(0xFF7A5900)
    val Wheat80 = Color(0xFFF5BD48)
    val Wheat90 = Color(0xFFFFDEA6)

    // Nötr / yüzey
    val Neutral10 = Color(0xFF1A1C19)
    val Neutral20 = Color(0xFF2F312D)
    val Neutral90 = Color(0xFFE2E3DD)
    val Neutral95 = Color(0xFFF0F1EB)
    val Surface = Color(0xFFFBF7F0)
    val SurfaceDark = Color(0xFF11140F)

    // Hata
    val Error40 = Color(0xFFBA1A1A)
    val Error80 = Color(0xFFFFB4AB)

    val White = Color(0xFFFFFFFF)
}
