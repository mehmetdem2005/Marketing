package com.secal.core.domain.auth

/**
 * Domain kimlik modeli (saf Kotlin — sağlayıcıdan bağımsız). Supabase/gotrue tipleri
 * domain'e sızmaz; data katmanı UserInfo → [AuthUser] eşler (ADR-004 port soyutlaması).
 */
data class AuthUser(
    val id: String,
    val email: String?,
    val displayName: String?,
)

/**
 * Oturum durumu — UI bunu gözleyip auth/ana akış arasında yönlendirir.
 */
sealed interface AuthState {
    /** İlk değerlendirme / oturum yükleniyor. */
    data object Loading : AuthState
    data class Authenticated(val user: AuthUser) : AuthState
    data object Unauthenticated : AuthState
}
