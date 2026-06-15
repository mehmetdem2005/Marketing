package com.secal.feature.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.secal.core.domain.catalog.Product
import com.secal.core.ui.state.UiStateScaffold
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalStatusBadge
import com.secal.designsystem.component.PriceTag
import com.secal.designsystem.theme.LocalSpacing

/**
 * Ürün detay: görsel + ad + fiyat + mağaza + stok + açıklama + **sepete ekle** (RPC).
 * Ekleme geri bildirimi snackbar ile (mikro-etkileşim); "Sepete git" aksiyonu sepeti açar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cartStatus by viewModel.cartStatus.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(cartStatus) {
        when (cartStatus) {
            CartStatus.Added -> {
                val result = snackbar.showSnackbar(
                    message = "Ürün sepete eklendi",
                    actionLabel = "Sepete git",
                )
                if (result == SnackbarResult.ActionPerformed) onOpenCart()
                viewModel.consumeCartStatus()
            }
            CartStatus.Error -> {
                snackbar.showSnackbar("Sepete eklenemedi, tekrar dene")
                viewModel.consumeCartStatus()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Ürün") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        UiStateScaffold(
            state = state,
            modifier = Modifier.fillMaxSize().padding(padding),
            onRetry = viewModel::load,
        ) { product ->
            ProductDetailContent(
                product = product,
                adding = cartStatus == CartStatus.Adding,
                onAddToCart = viewModel::addToCart,
            )
        }
    }
}

@Composable
private fun ProductDetailContent(product: Product, adding: Boolean, onAddToCart: () -> Unit) {
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
        SecalStatusBadge(text = if (product.inStock) "Stokta" else "Tükendi")
        product.description?.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = spacing.xs),
            )
        }
        SecalButton(
            text = "Sepete ekle",
            onClick = onAddToCart,
            enabled = product.inStock && !adding,
            loading = adding,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.md),
        )
    }
}
