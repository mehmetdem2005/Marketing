package com.secal.feature.seller

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.secal.core.domain.catalog.Product
import com.secal.feature.seller.R
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalCard
import com.secal.designsystem.component.SecalTextField
import com.secal.designsystem.component.PriceTag
import com.secal.designsystem.theme.LocalSpacing

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
        Text(stringResource(R.string.seller_error_generic), style = MaterialTheme.typography.bodyLarge)
        SecalButton(text = stringResource(R.string.action_retry), onClick = onRetry)
    }
}

@Composable
private fun StoreSetupForm(state: SellerUiState, viewModel: SellerViewModel) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        Text(stringResource(R.string.seller_create_store_title), style = MaterialTheme.typography.titleLarge)
        Text(
            stringResource(R.string.seller_create_store_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SecalTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.seller_store_name),
            modifier = Modifier.fillMaxWidth(),
        )
        SecalTextField(
            value = state.city,
            onValueChange = viewModel::onCityChange,
            label = stringResource(R.string.seller_city),
            modifier = Modifier.fillMaxWidth(),
        )
        SecalTextField(
            value = state.businessRegNo,
            onValueChange = viewModel::onBusinessRegNoChange,
            label = stringResource(R.string.seller_business_reg_no),
            supportingText = stringResource(R.string.seller_business_reg_no_hint),
            modifier = Modifier.fillMaxWidth(),
        )
        SecalTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = stringResource(R.string.field_description),
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )
        SecalButton(
            text = stringResource(R.string.seller_create_store_button),
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
        SecalButton(
            text = stringResource(R.string.seller_add_product),
            onClick = onAddProduct,
            modifier = Modifier.fillMaxWidth(),
        )
        if (products.isEmpty()) {
            Text(
                stringResource(R.string.seller_no_products),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                items(products, key = { it.id }) { product ->
                    SecalCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(spacing.md)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            PriceTag(amountMinor = product.priceMinor, currencyCode = product.currency)
                            Text(
                                stringResource(R.string.seller_stock_format, product.stock),
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
