package com.secal.core.domain.catalog

/** Kategori (ağaç; [parentId] null = kök). Saf domain. */
data class Category(
    val id: String,
    val name: String,
    val slug: String,
    val parentId: String? = null,
    val sortOrder: Int = 0,
)

/** Ürün. Fiyat [priceMinor] = en küçük birim (kuruş) — kayan nokta YOK (para bütünlüğü). */
data class Product(
    val id: String,
    val name: String,
    val description: String? = null,
    val priceMinor: Long,
    val currency: String = "TRY",
    val stock: Int = 0,
    val unit: String? = null,
    val storeId: String,
    val storeName: String? = null,
    val categoryId: String? = null,
    val imageUrls: List<String> = emptyList(),
) {
    val primaryImageUrl: String? get() = imageUrls.firstOrNull()
    val inStock: Boolean get() = stock > 0
}

/** Ürün listeleme filtresi (keşif/arama). Hepsi opsiyonel; null = filtre yok. */
data class ProductQuery(
    val categoryId: String? = null,
    val search: String? = null,
)

/** Mağaza (satıcıya bağlı). Public alanlar paylaşılan zon. */
data class Store(
    val id: String,
    val name: String,
    val slug: String,
    val description: String? = null,
    val city: String? = null,
    val logoUrl: String? = null,
    val isActive: Boolean = true,
)
