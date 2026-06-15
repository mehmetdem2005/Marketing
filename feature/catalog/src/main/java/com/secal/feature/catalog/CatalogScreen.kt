package com.secal.feature.catalog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.secal.core.domain.catalog.Category
import com.secal.core.domain.catalog.Product
import com.secal.core.ui.state.UiStateScaffold
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalTextField
import com.secal.designsystem.component.ProductCard
import com.secal.designsystem.theme.LocalSpacing

/**
 * Katalog/keşif: arama + kategori filtre çubuğu + ürün grid (2 sütun). Ürün durumu 4-durum
 * scaffold ile (skeleton/boş/hata/içerik). Görseller Coil ile tembel yüklenir.
 */
@Composable
fun CatalogScreen(
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val spacing = LocalSpacing.current
    Column(modifier = modifier.fillMaxSize().padding(horizontal = spacing.md)) {
        SearchBar(
            value = state.search,
            onValueChange = viewModel::onSearchChange,
            onSubmit = viewModel::onSearchSubmit,
        )
        CategoryFilter(
            categories = state.categories,
            selectedId = state.selectedCategoryId,
            onSelect = viewModel::onCategorySelect,
        )
        UiStateScaffold(
            state = state.products,
            modifier = Modifier.fillMaxSize(),
            onRetry = viewModel::loadProducts,
        ) { products ->
            ProductGrid(products = products, onProductClick = onProductClick)
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        SecalTextField(
            value = value,
            onValueChange = onValueChange,
            label = "Ürün ara",
            modifier = Modifier.weight(1f),
        )
        SecalButton(text = "Ara", onClick = onSubmit)
    }
}

@Composable
private fun CategoryFilter(
    categories: List<Category>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        FilterChip(
            selected = selectedId == null,
            onClick = { onSelect(null) },
            label = { Text("Tümü") },
        )
        categories.forEach { category ->
            FilterChip(
                selected = selectedId == category.id,
                onClick = { onSelect(category.id) },
                label = { Text(category.name) },
            )
        }
    }
}

@Composable
private fun ProductGrid(
    products: List<Product>,
    onProductClick: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = spacing.sm),
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                title = product.name,
                sellerName = product.storeName ?: "SeçAl",
                amountMinor = product.priceMinor,
                onClick = { onProductClick(product.id) },
                image = {
                    AsyncImage(
                        model = product.primaryImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                },
            )
        }
    }
}
