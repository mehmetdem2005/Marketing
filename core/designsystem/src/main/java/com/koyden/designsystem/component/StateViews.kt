package com.koyden.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.koyden.designsystem.theme.LocalSpacing

/**
 * Boş durum (loading/empty/error/content → **empty**). Vektör ikon (emoji YASAK),
 * açıklayıcı başlık + opsiyonel eylem. İkon dekoratiftir (semantics temizlenir);
 * ekran okuyucu yalnız metni okur.
 */
@Composable
fun EmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    action: (@Composable () -> Unit)? = null,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterVertically),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null, // dekoratif
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        if (action != null) {
            Column(Modifier.padding(top = spacing.sm)) { action() }
        }
    }
}

/**
 * Hata durumu. Ham istisna/sağlayıcı detayı GÖSTERİLMEZ (güvenlik/ISO 25010);
 * çağıran AppError.type → yerelleştirilmiş mesaja eşler. Yeniden-dene opsiyonel.
 */
@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onRetry: (() -> Unit)? = null,
    retryText: String = "Yeniden dene",
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.xl)
            .clearAndSetSemantics { contentDescription = message },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterVertically),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error,
            )
        }
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        if (onRetry != null) {
            KoydenButton(
                text = retryText,
                onClick = onRetry,
                variant = KoydenButtonVariant.Secondary,
                modifier = Modifier.padding(top = spacing.sm),
            )
        }
    }
}
