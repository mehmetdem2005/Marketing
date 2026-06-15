package com.secal.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * SeçAl marka renk token'ları (tek kaynak — inline renk YASAK).
 * Marka: canlı orman yeşili + sıcak amber/terracotta aksan + temiz nötr yüzeyler.
 * Tam M3 rol seti (container/surface/outline) → bileşenler "düz" değil, kasıtlı/pro görünür.
 * Kontrast WCAG 2.2 AA (≥4.5:1 metin).
 */
internal object SecalPalette {
    // Birincil — canlı orman yeşili
    val Green10 = Color(0xFF00210D)
    val Green20 = Color(0xFF003919)
    val Green30 = Color(0xFF005226)
    val Green40 = Color(0xFF1B7A3D) // primary (light)
    val Green50 = Color(0xFF2E9650)
    val Green80 = Color(0xFF8FE0A3)
    val Green90 = Color(0xFFABF7BE)
    val Green95 = Color(0xFFD4FFDD)

    // İkincil — buğday/toprak
    val Wheat10 = Color(0xFF271900)
    val Wheat20 = Color(0xFF402D00)
    val Wheat40 = Color(0xFF7A5900)
    val Wheat80 = Color(0xFFF5BD48)
    val Wheat90 = Color(0xFFFFDEA6)

    // Üçüncül — sıcak terracotta/amber aksan (rozet/indirim/öne çıkarma)
    val Amber10 = Color(0xFF2E1500)
    val Amber40 = Color(0xFFC8741A)
    val Amber90 = Color(0xFFFFDCC2)

    // Nötr / yüzey (sıcak krem zemin + katmanlı container'lar)
    val Neutral10 = Color(0xFF191C19)
    val Neutral30 = Color(0xFF45483F)
    val Neutral50 = Color(0xFF74796D)
    val Neutral90 = Color(0xFFE2E3DD)
    val Background = Color(0xFFFCFDF7)
    val Surface = Color(0xFFFCFDF7)
    val SurfaceVariant = Color(0xFFDDE5D8)
    val OnSurfaceVariant = Color(0xFF424940)
    val SurfaceContainerLow = Color(0xFFF3F5EE)
    val SurfaceContainer = Color(0xFFEEF0E9)
    val SurfaceContainerHigh = Color(0xFFE8EAE3)
    val Outline = Color(0xFF727970)
    val OutlineVariant = Color(0xFFC1C9BC)

    // Karanlık tema yüzeyleri
    val SurfaceDark = Color(0xFF101410)
    val SurfaceContainerDark = Color(0xFF1C201B)
    val OnSurfaceDark = Color(0xFFE2E3DD)

    // Hata
    val Error40 = Color(0xFFBA1A1A)
    val Error80 = Color(0xFFFFB4AB)
    val ErrorContainer = Color(0xFFFFDAD6)
    val OnErrorContainer = Color(0xFF410002)

    val White = Color(0xFFFFFFFF)
    val Scrim = Color(0xFF000000)
}
