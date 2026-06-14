package com.koyden.designsystem.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koyden.designsystem.motion.MotionTokens
import com.koyden.designsystem.motion.rememberReduceMotion

/**
 * Yükleme iskeleti (shimmer). Boş ekran yerine algılanan performansı artırır
 * (ui-ux-advanced). **reduce-motion** açıkken shimmer durur, sade dolgu kalır.
 * Dekoratiftir; ekran okuyucudan gizlenir (boş semantics).
 */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
) {
    val reduce = rememberReduceMotion()
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)

    val brush: Brush = if (reduce) {
        Brush.linearGradient(listOf(base, base))
    } else {
        val transition = rememberInfiniteTransition(label = "skeletonShimmer")
        val x by transition.animateFloat(
            initialValue = -300f,
            targetValue = 900f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1100, easing = MotionTokens.EasingStandard),
                repeatMode = RepeatMode.Restart,
            ),
            label = "skeletonX",
        )
        Brush.horizontalGradient(
            colors = listOf(base, highlight, base),
            startX = x,
            endX = x + 300f,
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
            .semantics { }, // dekoratif — okunmaz
    )
}

/** Tek satır iskeleti (varsayılan tam genişlik); çağıran genişliği daraltabilir. */
@Composable
fun SkeletonLine(modifier: Modifier = Modifier, height: Dp = 16.dp) {
    Skeleton(modifier = modifier.fillMaxWidth().height(height), cornerRadius = 4.dp)
}
