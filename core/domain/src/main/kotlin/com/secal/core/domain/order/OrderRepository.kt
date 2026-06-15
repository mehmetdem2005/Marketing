package com.secal.core.domain.order

import com.secal.core.common.result.DataResult

/** Sipariş satırı — sipariş anındaki ad/fiyat SNAPSHOT'u (ürün sonradan değişse bile sabit). */
data class OrderItem(
    val id: String,
    val productName: String,
    val unitPriceMinor: Long,
    val quantity: Int,
) {
    val lineTotalMinor: Long get() = unitPriceMinor * quantity
}

/** Sipariş. [items] yalnız detay sorgusunda dolar; liste sorgusunda boş olabilir. */
data class Order(
    val id: String,
    val totalMinor: Long,
    val status: String,
    val createdAt: String,
    val items: List<OrderItem> = emptyList(),
)

/**
 * Sipariş **portu** (domain). Sipariş PII'dir (RLS: yalnız sahibi). Oluşturma atomik
 * `place_order` RPC ile yapılır (sepetten sipariş + stok düş + sepeti temizle).
 */
interface OrderRepository {
    /** Sepetten sipariş oluştur; yeni sipariş id'sini döner. */
    suspend fun placeOrder(): DataResult<String>

    /** Kullanıcının siparişleri (yeniden eskiye; satırsız özet). */
    suspend fun getMyOrders(): DataResult<List<Order>>

    /** Tek sipariş + satırları. */
    suspend fun getOrder(orderId: String): DataResult<Order>
}
