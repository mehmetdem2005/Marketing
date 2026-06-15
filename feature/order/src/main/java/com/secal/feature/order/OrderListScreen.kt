package com.secal.feature.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.secal.core.domain.order.Order
import com.secal.designsystem.component.PriceTag
import com.secal.designsystem.component.SecalCard
import com.secal.designsystem.component.SecalStatusBadge
import com.secal.designsystem.theme.LocalSpacing
import com.secal.core.ui.state.UiStateScaffold

/** Siparişlerim: geçmiş siparişler listesi. */
@Composable
fun OrderListScreen(
    onOrderClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val spacing = LocalSpacing.current
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.load() }
    UiStateScaffold(
        state = state,
        modifier = modifier.fillMaxSize(),
        onRetry = viewModel::load,
    ) { orders ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            items(orders, key = { it.id }) { order ->
                OrderRow(order = order, onClick = { onOrderClick(order.id) })
            }
        }
    }
}

@Composable
private fun OrderRow(order: Order, onClick: () -> Unit) {
    val spacing = LocalSpacing.current
    SecalCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Sipariş #${order.id.take(8)}", style = MaterialTheme.typography.titleSmall)
                SecalStatusBadge(text = orderStatusLabel(order.status))
            }
            Text(
                order.createdAt.take(10),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PriceTag(amountMinor = order.totalMinor)
        }
    }
}

/** Sipariş durumu → Türkçe etiket (ISO 9241 öz-betimleyicilik). */
internal fun orderStatusLabel(status: String): String = when (status) {
    "pending" -> "Alındı"
    "confirmed" -> "Onaylandı"
    "shipped" -> "Kargoda"
    "delivered" -> "Teslim edildi"
    "cancelled" -> "İptal edildi"
    else -> status
}
