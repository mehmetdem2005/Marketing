package com.koyden.feature.seller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.catalog.Category
import com.koyden.core.domain.catalog.usecase.GetCategoriesUseCase
import com.koyden.core.domain.seller.NewProduct
import com.koyden.core.domain.seller.SellerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToLong

/** Ürün ekleme formu. Fiyat "12,50" gibi girilir → kuruş (Long). Görsel önce Storage'a yüklenir. */
data class AddProductUiState(
    val categories: List<Category> = emptyList(),
    val name: String = "",
    val price: String = "",
    val stock: String = "",
    val unit: String = "",
    val description: String = "",
    val selectedCategoryId: String? = null,
    val imageUrl: String? = null,
    val uploading: Boolean = false,
    val saving: Boolean = false,
    val error: AppError? = null,
    val done: Boolean = false,
) {
    val canSave: Boolean get() = name.isNotBlank() && priceMinor() != null && !saving && !uploading
    fun priceMinor(): Long? =
        price.trim().replace(',', '.').toDoubleOrNull()?.let { (it * 100).roundToLong() }
}

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val seller: SellerRepository,
    private val getCategories: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val storeId: String = checkNotNull(savedStateHandle[ARG_STORE_ID]) {
        "storeId navigasyon argümanı eksik"
    }

    private val _state = MutableStateFlow(AddProductUiState())
    val state: StateFlow<AddProductUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            (getCategories() as? DataResult.Success)?.let { result ->
                _state.update { it.copy(categories = result.data) }
            }
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onPriceChange(v: String) = _state.update { it.copy(price = v) }
    fun onStockChange(v: String) = _state.update { it.copy(stock = v) }
    fun onUnitChange(v: String) = _state.update { it.copy(unit = v) }
    fun onDescriptionChange(v: String) = _state.update { it.copy(description = v) }
    fun onCategorySelect(id: String?) = _state.update { it.copy(selectedCategoryId = id) }

    fun onImagePicked(bytes: ByteArray, extension: String) {
        _state.update { it.copy(uploading = true, error = null) }
        viewModelScope.launch {
            _state.update {
                when (val result = seller.uploadProductImage(storeId, bytes, extension)) {
                    is DataResult.Success -> it.copy(uploading = false, imageUrl = result.data)
                    is DataResult.Failure -> it.copy(uploading = false, error = result.error)
                }
            }
        }
    }

    fun createProduct() {
        val current = _state.value
        val priceMinor = current.priceMinor() ?: return
        if (!current.canSave) return
        _state.update { it.copy(saving = true, error = null) }
        viewModelScope.launch {
            val draft = NewProduct(
                storeId = storeId,
                name = current.name,
                priceMinor = priceMinor,
                stock = current.stock.trim().toIntOrNull() ?: 0,
                unit = current.unit,
                categoryId = current.selectedCategoryId,
                description = current.description,
                imageUrl = current.imageUrl,
            )
            _state.update {
                when (val result = seller.createProduct(draft)) {
                    is DataResult.Success -> it.copy(saving = false, done = true)
                    is DataResult.Failure -> it.copy(saving = false, error = result.error)
                }
            }
        }
    }

    companion object {
        const val ARG_STORE_ID = "storeId"
    }
}
