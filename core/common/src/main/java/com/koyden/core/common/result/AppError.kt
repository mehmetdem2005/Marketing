package com.koyden.core.common.result

/**
 * Uygulama genelinde tek hata modeli. UI, kullanıcıya gösterilecek mesajı
 * bu sınıflandırmaya göre seçer (ISO 25010 — Kullanılabilirlik/Güvenilirlik).
 *
 * Not: Ham istisna mesajları (stack trace, sağlayıcı detayları) kullanıcıya
 * gösterilmez; yalnızca [type] eşlenerek yerelleştirilmiş metin üretilir.
 */
data class AppError(
    val type: ErrorType,
    val message: String? = null,
    val cause: Throwable? = null,
)

enum class ErrorType {
    NETWORK,
    TIMEOUT,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    VALIDATION,
    CONFLICT,
    SERVER,
    UNKNOWN,
}
