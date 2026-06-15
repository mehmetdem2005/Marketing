package com.secal.core.domain.profile

/**
 * Kullanıcı rolü (DB `public.user_role` enum karşılığı). Saf domain — sağlayıcıdan bağımsız.
 */
enum class UserRole {
    BUYER,
    SELLER,
    ADMIN,
}

/**
 * Domain profil modeli (PII). `profiles` tablosu + auth'tan e-posta birleştirilir.
 * Rol değiştirme istemciden YAPILMAZ (RLS/güvenlik); yalnız [fullName]/[phone] düzenlenir.
 */
data class Profile(
    val id: String,
    val email: String?,
    val role: UserRole,
    val fullName: String?,
    val phone: String?,
    val avatarUrl: String?,
)

/** Profil düzenleme girdisi için ad doğrulaması (DB: full_name <= 120). */
object ProfileValidation {
    const val FULL_NAME_MAX = 120

    /** Boş ad null'a indirgenir; çok uzun ad reddedilir. */
    fun normalizeFullName(raw: String): String? = raw.trim().ifBlank { null }

    fun isFullNameValid(raw: String): Boolean =
        raw.trim().length <= FULL_NAME_MAX

    fun normalizePhone(raw: String): String? = raw.trim().ifBlank { null }
}
