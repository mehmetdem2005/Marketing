package com.secal.feature.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.secal.core.domain.cart.CartItem
import com.secal.designsystem.component.EmptyState
import com.secal.designsystem.component.PriceTag
import com.secal.designsystem.component.QuantityStepper
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalButtonVariant
import com.secal.designsystem.theme.LocalSpacing
import com.secal.core.ui.state.UiStateScaffold

/** Sepet ekranı: satırlar (adet/kaldır) + alt özet (toplam + sipariş — Faz 5b). */
@Composable
fun CartScreen(
    onExplore: () -> Unit,
    onOrderPlaced: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val checkout by viewModel.checkout.collectAsStateWithLifecycle()
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.load() }
    LaunchedEffect(checkout) {
        (checkout as? CheckoutUi.Placed)?.let {
            onOrderPlaced(it.orderId)
            viewModel.consumeCheckout()
        }
    }
    UiStateScaffold(
        state = state,
        modifier = modifier.fillMaxSize(),
        onRetry = viewModel::load,
        empty = {
            EmptyState(
                title = "Sepetin boş",
                description = "Ürünleri keşfet, beğendiğini sepete ekle.",
                action = { SecalButton(text = "Ürünleri keşfet", onClick = onExplore) },
            )
        },
    ) { items ->
        CartContent(
            items = items,
            placing = checkout is CheckoutUi.Placing,
            onQuantityChange = viewModel::changeQuantity,
            onRemove = viewModel::removeItem,
            onCheckout = viewModel::placeOrder,
        )
    }
}

@Composable
private fun CartContent(
    items: List<CartItem>,
    placing: Boolean,
    onQuantityChange: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    onCheckout: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val totalMinor = items.sumOf { it.lineTotalMinor }
    val currency = items.firstOrNull()?.product?.currency ?: "TRY"
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            items(items, key = { it.id }) { item ->
                CartRow(item = item, onQuantityChange = onQuantityChange, onRemove = onRemove)
                HorizontalDivider()
            }
        }
        CartSummary(totalMinor = totalMinor, currency = currency, placing = placing, onCheckout = onCheckout)
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    onQuantityChange: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = item.product.primaryImageUrl,
            contentDescription = item.product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Text(
                item.product.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
            )
            PriceTag(amountMinor = item.lineTotalMinor, currencyCode = item.product.currency)
            QuantityStepper(
                quantity = item.quantity,
                onQuantityChange = { onQuantityChange(item.id, it) },
                min = 1,
            )
        }
        SecalButton(
            text = "Kaldır",
            onClick = { onRemove(item.id) },
            variant = SecalButtonVariant.Text,
        )
    }
}

@Composable
private fun CartSummary(totalMinor: Long, currency: String, placing: Boolean, onCheckout: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Toplam", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            PriceTag(amountMinor = totalMinor, currencyCode = currency)
        }
        SecalButton(
            text = "Siparişi tamamla",
            onClick = onCheckout,
            enabled = !placing,
            loading = placing,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
