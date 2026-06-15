package com.koyden.feature.seller

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import com.koyden.core.domain.catalog.Product
import com.koyden.designsystem.component.KoydenButton
import com.koyden.designsystem.component.KoydenCard
import com.koyden.designsystem.component.KoydenTextField
import com.koyden.designsystem.component.PriceTag
import com.koyden.designsystem.theme.LocalSpacing

/** Satıcı paneli: mağaza yoksa kurulum formu; varsa ürün listesi + "Ürün ekle". */
@Composable
fun SellerScreen(
    onAddProduct: (storeId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SellerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val spacing = LocalSpacing.current
    // Ürün ekleyip döndüğünde listeyi tazele.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refreshProducts() }
    Box(modifier = modifier.fillMaxSize().padding(spacing.md), contentAlignment = Alignment.TopStart) {
        when (state.phase) {
            SellerPhase.Loading -> CenterLoader()
            SellerPhase.Error -> ErrorRetry(onRetry = viewModel::loadStore)
            SellerPhase.NeedsStore -> StoreSetupForm(state = state, viewModel = viewModel)
            SellerPhase.Ready -> ProductList(
                products = state.products,
                storeName = state.store?.name.orEmpty(),
                onAddProduct = { state.store?.let { onAddProduct(it.id) } },
            )
        }
    }
}

@Composable
private fun CenterLoader() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorRetry(onRetry: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        Text("Bir şeyler ters gitti.", style = MaterialTheme.typography.bodyLarge)
        KoydenButton(text = "Tekrar dene", onClick = onRetry)
    }
}

@Composable
private fun StoreSetupForm(state: SellerUiState, viewModel: SellerViewModel) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        Text("Mağazanı oluştur", style = MaterialTheme.typography.titleLarge)
        Text(
            "Satış yapmak için önce mağazanı aç.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        KoydenTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = "Mağaza adı",
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenTextField(
            value = state.city,
            onValueChange = viewModel::onCityChange,
            label = "Şehir",
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = "Açıklama",
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenButton(
            text = "Mağazayı oluştur",
            onClick = viewModel::createStore,
            enabled = state.name.isNotBlank() && !state.saving,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.sm),
        )
    }
}

@Composable
private fun ProductList(products: List<Product>, storeName: String, onAddProduct: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        Text(storeName, style = MaterialTheme.typography.titleLarge)
        KoydenButton(
            text = "+ Ürün ekle",
            onClick = onAddProduct,
            modifier = Modifier.fillMaxWidth(),
        )
        if (products.isEmpty()) {
            Text(
                "Henüz ürün yok. İlk ürününü ekle.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                items(products, key = { it.id }) { product ->
                    KoydenCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(spacing.md)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            PriceTag(amountMinor = product.priceMinor, currencyCode = product.currency)
                            Text(
                                "Stok: ${product.stock}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}
