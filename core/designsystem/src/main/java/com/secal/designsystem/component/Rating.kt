package com.secal.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Yıldız değerlendirme (molekül). **Vektör ikon** (emoji YASAK). Yarım yıldız desteklenir.
 * Tüm grup tek bir erişilebilirlik düğümü olarak okunur (ör. "4,5 / 5, 128 değerlendirme");
 * tek tek yıldızlar ekran okuyucuyu boğmaz.
 */
@Composable
fun Rating(
    rating: Float,
    modifier: Modifier = Modifier,
    count: Int? = null,
    starSize: Dp = 16.dp,
    showValue: Boolean = true,
) {
    val clamped = rating.coerceIn(0f, 5f)
    val description = buildString {
        append("${"%.1f".format(clamped)} / 5 yıldız")
        if (count != null) append(", $count değerlendirme")
    }
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        val halves = (clamped * 2).roundToInt() // 0..10
        for (i in 1..5) {
            val icon = when {
                halves >= i * 2 -> Icons.Filled.Star
                halves == i * 2 - 1 -> Icons.Filled.StarHalf
                else -> Icons.Outlined.StarOutline
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(starSize),
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        if (showValue) {
            Text(
                text = buildString {
                    append(" ${"%.1f".format(clamped)}")
                    if (count != null) append(" ($count)")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
