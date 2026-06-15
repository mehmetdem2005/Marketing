package com.secal.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.secal.designsystem.motion.MotionTokens
import com.secal.designsystem.motion.rememberReduceMotion

enum class SecalButtonVariant { Primary, Secondary, Text }

/**
 * SeçAl birincil eylem butonu (atom). Material 3 üstüne marka davranışı:
 *  - **Press micro-etkileşimi:** basılıyken hafif scale (0.96) — motion-design "button press".
 *    reduce-motion açıkken animasyon devre dışı.
 *  - **Dokunma hedefi ≥48dp** (WCAG 2.2 / dokunma hedefi).
 *  - **loading:** spinner gösterir, tıklamayı kilitler (çift gönderim önleme).
 *
 * Etiket metni anlamlı olmalı; ikon-only kullanımda çağıran contentDescription verir.
 */
@Composable
fun SecalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: SecalButtonVariant = SecalButtonVariant.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val reduce = rememberReduceMotion()
    val scale by animateFloatAsState(
        targetValue = if (pressed && !reduce) 0.96f else 1f,
        animationSpec = tween(
            durationMillis = if (reduce) 0 else MotionTokens.DurationQuick,
            easing = MotionTokens.EasingStandard,
        ),
        label = "buttonPressScale",
    )

    val pressMod = modifier
        .graphicsLayer { scaleX = scale; scaleY = scale }
        .heightIn(min = 48.dp)

    val content: @Composable () -> Unit = {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Text(text)
        }
    }

    when (variant) {
        SecalButtonVariant.Primary -> Button(
            onClick = onClick, modifier = pressMod, enabled = enabled && !loading,
            interactionSource = interaction,
        ) { content() }
        SecalButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick, modifier = pressMod, enabled = enabled && !loading,
            interactionSource = interaction,
        ) { content() }
        SecalButtonVariant.Text -> TextButton(
            onClick = onClick, modifier = pressMod, enabled = enabled && !loading,
            interactionSource = interaction,
        ) { content() }
    }
}
