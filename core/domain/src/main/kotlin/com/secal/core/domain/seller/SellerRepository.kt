package com.secal.core.domain.seller

import com.secal.core.common.result.DataResult
import com.secal.core.domain.catalog.Product
import com.secal.core.domain.catalog.Store

/** Yeni ürün taslağı (satıcı formu → repository). Fiyat kuruş. */
data class NewProduct(
    val storeId: String,
    val name: String,
    val priceMinor: Long,
    val stock: Int,
    val unit: String? = null,
    val categoryId: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
)

/**
 * Satıcı **portu** (domain). Mağaza sahibi RLS'iyle kendi mağaza/ürününü yönetir.
 * Görsel yükleme `product-images` Storage bucket'ına yapılır; dönen public URL ürüne işlenir.
 */
interface SellerRepository {
    /** Aktif kullanıcının mağazası (yoksa null). */
    suspend fun getMyStore(): DataResult<Store?>

    /** Mağaza oluştur (owner = aktif kullanıcı). */
    suspend fun createStore(name: String, city: String?, description: String?): DataResult<Store>

    /** Mağazanın ürünleri (satıcı görünümü — aktif/pasif tümü). */
    suspend fun getMyProducts(storeId: String): DataResult<List<Product>>

    /** Ürün oluştur (taslaktan). */
    suspend fun createProduct(draft: NewProduct): DataResult<Product>

    /** Görseli Storage'a yükle; public URL döner. [extension] ör. "jpg". */
    suspend fun uploadProductImage(storeId: String, bytes: ByteArray, extension: String): DataResult<String>
}
