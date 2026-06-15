package com.koyden.feature.seller

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.koyden.designsystem.component.KoydenButton
import com.koyden.designsystem.component.KoydenButtonVariant
import com.koyden.designsystem.component.KoydenTextField
import com.koyden.designsystem.theme.LocalSpacing

/** Ürün ekleme formu: alanlar + kategori seçimi + görsel yükleme (Storage). */
@Composable
fun AddProductScreen(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddProductViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            val ext = context.contentResolver.getType(uri)?.substringAfterLast('/') ?: "jpg"
            if (bytes != null) viewModel.onImagePicked(bytes, ext)
        }
    }

    LaunchedEffect(state.done) {
        if (state.done) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text("Yeni ürün", style = MaterialTheme.typography.titleLarge)

        ImagePicker(
            imageUrl = state.imageUrl,
            uploading = state.uploading,
            onPick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        )

        KoydenTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = "Ürün adı",
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenTextField(
            value = state.price,
            onValueChange = viewModel::onPriceChange,
            label = "Fiyat (₺)",
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenTextField(
            value = state.stock,
            onValueChange = viewModel::onStockChange,
            label = "Stok adedi",
            keyboardType = KeyboardType.Number,
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenTextField(
            value = state.unit,
            onValueChange = viewModel::onUnitChange,
            label = "Birim (kg, adet, litre…)",
            modifier = Modifier.fillMaxWidth(),
        )
        CategoryPicker(
            state = state,
            onSelect = viewModel::onCategorySelect,
        )
        KoydenTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = "Açıklama",
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )
        KoydenButton(
            text = if (state.saving) "Kaydediliyor…" else "Ürünü yayınla",
            onClick = viewModel::createProduct,
            enabled = state.canSave,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.sm),
        )
    }
}

@Composable
private fun ImagePicker(imageUrl: String?, uploading: Boolean, onPick: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Ürün görseli",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
            )
        }
        KoydenButton(
            text = when {
                uploading -> "Yükleniyor…"
                imageUrl != null -> "Görseli değiştir"
                else -> "Görsel ekle"
            },
            onClick = onPick,
            variant = KoydenButtonVariant.Secondary,
            enabled = !uploading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CategoryPicker(state: AddProductUiState, onSelect: (String?) -> Unit) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        state.categories.forEach { category ->
            FilterChip(
                selected = state.selectedCategoryId == category.id,
                onClick = {
                    onSelect(if (state.selectedCategoryId == category.id) null else category.id)
                },
                label = { Text(category.name) },
            )
        }
    }
}
