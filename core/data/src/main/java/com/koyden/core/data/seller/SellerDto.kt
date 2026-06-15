package com.koyden.core.data.seller

import com.koyden.core.domain.catalog.Store
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StoreInsertDto(
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val slug: String,
    val city: String? = null,
    val description: String? = null,
)

@Serializable
internal data class StoreRowDto(
    val id: String,
    val name: String,
    val slug: String,
    val description: String? = null,
    val city: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
)

@Serializable
internal data class ProductInsertDto(
    @SerialName("store_id") val storeId: String,
    val name: String,
    val slug: String,
    @SerialName("price_minor") val priceMinor: Long,
    val stock: Int,
    val unit: String? = null,
    @SerialName("category_id") val categoryId: String? = null,
    val description: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
)

@Serializable
internal data class ProductImageInsertDto(
    @SerialName("product_id") val productId: String,
    val url: String,
    @SerialName("sort_order") val sortOrder: Int = 0,
)

internal fun StoreRowDto.toDomain(): Store = Store(
    id = id,
    name = name,
    slug = slug,
    description = description,
    city = city,
    logoUrl = logoUrl,
    isActive = isActive,
)
