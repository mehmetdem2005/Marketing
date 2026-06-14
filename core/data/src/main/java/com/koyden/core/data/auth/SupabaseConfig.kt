package com.koyden.core.data.auth

/**
 * Supabase bağlantı yapılandırması. Değerler **app BuildConfig**'ten (local.properties / CI
 * secret) gelir; repoda sır tutulmaz (ADR-005). Anon key tasarımı gereği herkese açıktır,
 * güvenlik RLS ile sağlanır.
 */
data class SupabaseConfig(
    val url: String,
    val anonKey: String,
)

/**
 * Auth sağlayıcı yapılandırması. [googleServerClientId] = Google Cloud "Web" OAuth istemci
 * kimliği (Supabase Google sağlayıcısıyla aynı). BuildConfig'ten gelir.
 */
data class AuthConfig(
    val googleServerClientId: String,
)
