package com.koyden.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.koyden.designsystem.theme.LocalSpacing

/**
 * Ürün kartı (organizma) — katalog/grid. Görsel **slot** olarak alınır ([image]) →
 * designsystem görüntü kütüphanesinden (Coil) bağımsız kalır; çağıran (core:ui/feature)
 * gerçek resmi sağlar. Kart bir bütün olarak okunur ve tıklanır.
 *
 * @param amountMinor fiyat (kuruş). @param rating 0..5. @param badge sağ-üst rozet (ör. "Yeni").
 */
@Composable
fun ProductCard(
    title: String,
    sellerName: String,
    amountMinor: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    rating: Float? = null,
    ratingCount: Int? = null,
    originalAmountMinor: Long? = null,
    badge: String? = null,
    image: @Composable () -> Unit = {},
) {
    val spacing = LocalSpacing.current
    val a11y = buildString {
        append("$title, $sellerName")
        if (rating != null) append(", ${"%.1f".format(rating)} yıldız")
    }
    KoydenCard(
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = a11y },
        onClick = onClick,
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) { image() }
            if (badge != null) {
                KoydenStatusBadge(
                    text = badge,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing.xs),
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = spacing.sm),
        )
        Text(
            text = sellerName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (rating != null) {
            Rating(
                rating = rating,
                count = ratingCount,
                modifier = Modifier.padding(top = spacing.xxs),
            )
        }
        PriceTag(
            amountMinor = amountMinor,
            originalAmountMinor = originalAmountMinor,
            modifier = Modifier.padding(top = spacing.xs),
        )
    }
}
