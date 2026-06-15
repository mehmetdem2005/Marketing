package com.secal.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secal.designsystem.theme.LocalSpacing

/**
 * SeçAl yüzey kartı (atom/kapsayıcı). 8pt grid iç boşluk, marka köşe yarıçapı.
 * Tıklanabilirse [onClick] verilir (rol/erişilebilirlik M3 Card'dan gelir).
 */
@Composable
fun SecalCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val spacing = LocalSpacing.current
    val shape = RoundedCornerShape(16.dp)
    val colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )
    val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier, shape = shape, colors = colors, elevation = elevation) {
            Column(Modifier.padding(spacing.md), content = content)
        }
    } else {
        Card(modifier = modifier, shape = shape, colors = colors, elevation = elevation) {
            Column(Modifier.padding(spacing.md), content = content)
        }
    }
}
