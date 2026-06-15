package com.secal.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.secal.feature.home.R
import com.secal.core.domain.catalog.Category
import com.secal.core.domain.catalog.Product
import com.secal.designsystem.component.ProductCard
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.theme.LocalSpacing

/** Anasayfa: arama + marka banner + kategori şeridi + öne çıkan ürünler (Trendyol kalıbı). */
@Composable
fun HomeScreen(
    onSearch: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onBecomeSeller: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = spacing.xl),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        item { SearchBar(onSearch = onSearch) }
        item { HeroBanner() }
        if (state.loading) {
            item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.md)) }
        }
        if (state.categories.isNotEmpty()) {
            item {
                SectionHeader(title = stringResource(R.string.home_section_categories))
                CategoryStrip(categories = state.categories, onCategoryClick = onCategoryClick)
            }
        }
        item {
            SectionHeader(
                title = stringResource(R.string.home_section_featured),
                actionLabel = stringResource(R.string.action_see_all),
                onAction = onSearch,
            )
        }
        if (state.featured.isNotEmpty()) {
            item { FeaturedRow(products = state.featured, onProductClick = onProductClick) }
        } else if (!state.loading) {
            item { EmptyFeatured(onBecomeSeller = onBecomeSeller) }
        }
    }
}

@Composable
private fun SearchBar(onSearch: () -> Unit) {
    val spacing = LocalSpacing.current
    Surface(
        onClick = onSearch,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.md, vertical = spacing.sm),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = spacing.md, vertical = spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                stringResource(R.string.home_search_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HeroBanner() {
    val spacing = LocalSpacing.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.md)
            .clip(RoundedCornerShape(24.dp))
            .height(170.dp)
            .background(MaterialTheme.colorScheme.primary),
    ) {
        AsyncImage(
            model = imageUrl("turkish,bazaar,food", 900, 500),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        // Okunabilirlik için koyu degrade scrim (marka rengi değil → fonksiyonel overlay).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Black.copy(alpha = 0.55f), Color.Transparent),
                    ),
                ),
        )
        // Marka wordmark — slogan YOK (kullanıcı onayı olmadan metin yazılmaz).
        Text(
            stringResource(R.string.home_brand),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart).padding(spacing.lg),
        )
    }
}

@Composable
private fun SectionHeader(title: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.md, vertical = spacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        if (actionLabel != null && onAction != null) {
            Text(
                actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onAction).padding(spacing.xs),
            )
        }
    }
}

@Composable
private fun CategoryStrip(categories: List<Category>, onCategoryClick: (String) -> Unit) {
    val spacing = LocalSpacing.current
    LazyRow(
        contentPadding = PaddingValues(horizontal = spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryItem(category = category, onClick = { onCategoryClick(category.id) })
        }
    }
}

@Composable
private fun CategoryItem(category: Category, onClick: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.width(84.dp).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
        ) {
            AsyncImage(
                model = imageUrl(categoryKeyword(category.slug), 200, 200),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Text(
            category.name,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Kategori slug → topical foto anahtar kelimesi (LoremFlickr). */
private fun categoryKeyword(slug: String): String = when {
    "bal" in slug || "ari" in slug -> "honey,jar"
    "zeytin" in slug -> "olive,oil"
    "peynir" in slug || "sut" in slug -> "cheese,dairy"
    "kuruyemis" in slug || "kurutul" in slug -> "nuts,dried"
    "bakliyat" in slug || "tahil" in slug -> "legumes,grain"
    "recel" in slug || "salca" in slug -> "jam,tomato"
    "baharat" in slug || "cay" in slug -> "spices,tea"
    "sanat" in slug -> "handmade,craft"
    else -> "food,natural"
}

/** LoremFlickr topical foto URL'i (anahtar kelimeyle gerçek fotoğraf; ücretsiz/CC). */
private fun imageUrl(keywords: String, width: Int, height: Int): String =
    "https://loremflickr.com/$width/$height/$keywords"

@Composable
private fun FeaturedRow(products: List<Product>, onProductClick: (String) -> Unit) {
    val spacing = LocalSpacing.current
    LazyRow(
        contentPadding = PaddingValues(horizontal = spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                title = product.name,
                sellerName = product.storeName ?: "SeçAl",
                amountMinor = product.priceMinor,
                onClick = { onProductClick(product.id) },
                modifier = Modifier.width(160.dp),
                image = {
                    AsyncImage(
                        model = product.primaryImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
            )
        }
    }
}

@Composable
private fun EmptyFeatured(onBecomeSeller: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text(
            stringResource(R.string.home_empty_products),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        SecalButton(text = stringResource(R.string.home_empty_seller_cta), onClick = onBecomeSeller)
    }
}
