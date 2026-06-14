package com.koyden.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.koyden.designsystem.motion.MotionTokens
import com.koyden.designsystem.motion.motionDuration

/**
 * Adet artır/azalt (molekül) — sepet/ürün. Sınırlara ([min]..[max]) saygı; butonlar
 * sınırda devre dışı. Dokunma hedefi 48dp. Sayı değişimi reduce-motion'a saygılı fade.
 * İkonlar dekoratif; butonlara anlamlı contentDescription verilir.
 */
@Composable
fun QuantityStepper(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 0,
    max: Int = 99,
) {
    val canDec = quantity > min
    val canInc = quantity < max
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        FilledTonalIconButton(
            onClick = { if (canDec) onQuantityChange(quantity - 1) },
            enabled = canDec,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(Icons.Filled.Remove, contentDescription = "Adedi azalt")
        }

        AnimatedContent(
            targetState = quantity,
            transitionSpec = {
                val d = motionDuration(MotionTokens.DurationQuick)
                (fadeIn(tween(d)) togetherWith fadeOut(tween(d)))
            },
            label = "quantityValue",
        ) { value ->
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 32.dp),
            )
        }

        FilledTonalIconButton(
            onClick = { if (canInc) onQuantityChange(quantity + 1) },
            enabled = canInc,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Adedi artır")
        }
    }
}
