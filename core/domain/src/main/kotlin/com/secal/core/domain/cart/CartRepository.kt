package com.secal.core.domain.cart

import com.secal.core.common.result.DataResult
import com.secal.core.domain.catalog.Product

/** Sepet satırı — ürün + adet. Satır toplamı kuruş tabanlı (kayan nokta yok). */
data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int,
) {
    val lineTotalMinor: Long get() = product.priceMinor * quantity
}

/**
 * Sepet **portu** (domain). Sepet PII'dir; RLS ile yalnız sahibine açıktır.
 * Ekleme atomik RPC (`add_to_cart`) ile yapılır — varsa miktar artar.
 */
interface CartRepository {
    suspend fun getCart(): DataResult<List<CartItem>>
    suspend fun addToCart(productId: String, quantity: Int = 1): DataResult<Unit>
    suspend fun updateQuantity(itemId: String, quantity: Int): DataResult<Unit>
    suspend fun removeItem(itemId: String): DataResult<Unit>
}
