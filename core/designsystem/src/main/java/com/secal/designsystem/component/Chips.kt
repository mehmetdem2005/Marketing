package com.secal.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Filtre/kategori chip'i (atom). Seçili durumu görsel + erişilebilirlik olarak taşır.
 */
@Composable
fun SecalFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        label = { Text(label) },
    )
}

/**
 * Sayaç rozeti (badge) — ör. sepet adedi. 99 üstü "99+" olarak kısaltılır.
 * Ekran okuyucuya anlamlı metinle ([contentDescription]) bildirilir.
 */
@Composable
fun SecalCountBadge(
    count: Int,
    modifier: Modifier = Modifier,
    description: String = "$count öğe",
) {
    if (count <= 0) return
    val text = if (count > 99) "99+" else count.toString()
    Badge(
        modifier = modifier.semantics { contentDescription = description },
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
    ) { Text(text) }
}

/**
 * Durum rozeti (ör. "Yeni", "Stokta yok"). Renk semantiği çağırandan gelir.
 */
@Composable
fun SecalStatusBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .semantics { contentDescription = text }
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}
