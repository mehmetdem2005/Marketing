package com.secal.core.data.catalog

import com.secal.core.domain.catalog.Category
import com.secal.core.domain.catalog.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CategoryDto(
    val id: String,
    val name: String,
    val slug: String,
    @SerialName("parent_id") val parentId: String? = null,
    @SerialName("sort_order") val sortOrder: Int = 0,
)

@Serializable
internal data class StoreNameDto(val name: String? = null)

@Serializable
internal data class ProductImageDto(
    val url: String,
    @SerialName("sort_order") val sortOrder: Int = 0,
)

/** `products` satırı + gömülü `stores(name)` (to-one) + `product_images(...)` (to-many). */
@Serializable
internal data class ProductRowDto(
    val id: String,
    val name: String,
    val description: String? = null,
    @SerialName("price_minor") val priceMinor: Long = 0,
    val currency: String = "TRY",
    val stock: Int = 0,
    val unit: String? = null,
    @SerialName("store_id") val storeId: String,
    @SerialName("category_id") val categoryId: String? = null,
    val stores: StoreNameDto? = null,
    @SerialName("product_images") val productImages: List<ProductImageDto> = emptyList(),
)

internal fun CategoryDto.toDomain(): Category =
    Category(id = id, name = name, slug = slug, parentId = parentId, sortOrder = sortOrder)

internal fun ProductRowDto.toDomain(): Product = Product(
    id = id,
    name = name,
    description = description,
    priceMinor = priceMinor,
    currency = currency,
    stock = stock,
    unit = unit,
    storeId = storeId,
    storeName = stores?.name,
    categoryId = categoryId,
    imageUrls = productImages.sortedBy { it.sortOrder }.map { it.url },
)

/** products için gömülü select sütun ifadesi (mağaza adı + görseller). */
internal const val PRODUCT_COLUMNS: String =
    "id,name,description,price_minor,currency,stock,unit,store_id,category_id," +
        "stores(name),product_images(url,sort_order)"
