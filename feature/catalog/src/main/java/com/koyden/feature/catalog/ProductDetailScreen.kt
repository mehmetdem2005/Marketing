package com.koyden.feature.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.koyden.core.domain.catalog.Product
import com.koyden.core.ui.state.UiStateScaffold
import com.koyden.designsystem.component.KoydenButton
import com.koyden.designsystem.component.KoydenStatusBadge
import com.koyden.designsystem.component.PriceTag
import com.koyden.designsystem.theme.LocalSpacing

/**
 * Ürün detay: görsel + ad + fiyat + mağaza + stok + açıklama + (sepete ekle — Faz: sepet).
 * Yükleme 4-durum scaffold ile.
 */
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    UiStateScaffold(
        state = state,
        modifier = modifier.fillMaxSize(),
        onRetry = viewModel::load,
    ) { product ->
        ProductDetailContent(product = product, onBack = onBack)
    }
}

@Composable
private fun ProductDetailContent(product: Product, onBack: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        AsyncImage(
            model = product.primaryImageUrl,
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
        )
        Text(text = product.name, style = MaterialTheme.typography.titleLarge)
        product.storeName?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        PriceTag(amountMinor = product.priceMinor, currencyCode = product.currency)
        KoydenStatusBadge(text = if (product.inStock) "Stokta" else "Tükendi")
        product.description?.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = spacing.xs),
            )
        }
        KoydenButton(
            text = "Sepete ekle",
            onClick = onBack, // Faz: sepet — şimdilik geri döner (placeholder akış)
            enabled = product.inStock,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.md),
        )
    }
}
