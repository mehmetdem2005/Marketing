package com.secal.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * SeçAl şekil token'ları (M3 Shapes). Yuvarlatılmış, modern/pro his.
 * Bileşenler `MaterialTheme.shapes` üzerinden bunları kullanır (kart/buton/chip/sheet).
 */
val SecalShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
