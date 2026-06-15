package com.secal.feature.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.secal.core.domain.order.Order
import com.secal.core.domain.order.OrderItem
import com.secal.designsystem.component.PriceTag
import com.secal.designsystem.component.SecalStatusBadge
import com.secal.designsystem.theme.LocalSpacing
import com.secal.core.ui.state.UiStateScaffold

/** Sipariş detayı / onayı: durum + satırlar + toplam. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrderDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Sipariş detayı") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
            )
        },
    ) { padding ->
        UiStateScaffold(
            state = state,
            modifier = Modifier.fillMaxSize().padding(padding),
            onRetry = viewModel::load,
        ) { order ->
            OrderDetailContent(order = order)
        }
    }
}

@Composable
private fun OrderDetailContent(order: Order) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Sipariş #${order.id.take(8)}", style = MaterialTheme.typography.titleMedium)
            SecalStatusBadge(text = orderStatusLabel(order.status))
        }
        Text(
            order.createdAt.take(10),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = spacing.xs))
        order.items.forEach { item -> OrderItemRow(item = item) }
        HorizontalDivider(modifier = Modifier.padding(vertical = spacing.xs))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Toplam", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            PriceTag(amountMinor = order.totalMinor)
        }
    }
}

@Composable
private fun OrderItemRow(item: OrderItem) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "${item.productName}  ×${item.quantity}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f).padding(end = spacing.sm),
        )
        PriceTag(amountMinor = item.lineTotalMinor)
    }
}
