package com.secal.feature.seller

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalButtonVariant
import com.secal.designsystem.component.SecalTextField
import com.secal.designsystem.theme.LocalSpacing
import com.secal.feature.seller.R

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
        Text(stringResource(R.string.seller_new_product), style = MaterialTheme.typography.titleLarge)

        ImagePicker(
            imageUrl = state.imageUrl,
            uploading = state.uploading,
            onPick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        )

        SecalTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.seller_product_name),
            modifier = Modifier.fillMaxWidth(),
        )
        SecalTextField(
            value = state.price,
            onValueChange = viewModel::onPriceChange,
            label = stringResource(R.string.seller_price),
            keyboardType = KeyboardType.Decimal,
            modifier = Modifier.fillMaxWidth(),
        )
        SecalTextField(
            value = state.stock,
            onValueChange = viewModel::onStockChange,
            label = stringResource(R.string.seller_stock_qty),
            keyboardType = KeyboardType.Number,
            modifier = Modifier.fillMaxWidth(),
        )
        SecalTextField(
            value = state.unit,
            onValueChange = viewModel::onUnitChange,
            label = stringResource(R.string.seller_unit),
            modifier = Modifier.fillMaxWidth(),
        )
        CategoryPicker(
            state = state,
            onSelect = viewModel::onCategorySelect,
        )
        SecalTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = stringResource(R.string.field_description),
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )
        SecalButton(
            text = if (state.saving) stringResource(R.string.seller_publishing) else stringResource(R.string.seller_publish),
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
                contentDescription = stringResource(R.string.seller_product_image_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
            )
        }
        SecalButton(
            text = when {
                uploading -> stringResource(R.string.seller_image_uploading)
                imageUrl != null -> stringResource(R.string.seller_image_change)
                else -> stringResource(R.string.seller_image_add)
            },
            onClick = onPick,
            variant = SecalButtonVariant.Secondary,
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
