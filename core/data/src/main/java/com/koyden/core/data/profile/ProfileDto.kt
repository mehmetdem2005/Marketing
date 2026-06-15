package com.koyden.core.data.profile

import com.koyden.core.domain.profile.Profile
import com.koyden.core.domain.profile.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** `profiles` satırı (okuma). E-posta auth'tan ayrı eklenir (bu tabloda yok). */
@Serializable
internal data class ProfileDto(
    val id: String,
    val role: String,
    @SerialName("full_name") val fullName: String? = null,
    val phone: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

/** `profiles` kısmi güncelleme gövdesi (yalnız ad/telefon; null'lar sütunu temizler). */
@Serializable
internal data class ProfileUpdateDto(
    @SerialName("full_name") val fullName: String?,
    val phone: String?,
)

internal fun ProfileDto.toDomain(email: String?): Profile = Profile(
    id = id,
    email = email,
    role = role.toUserRole(),
    fullName = fullName,
    phone = phone,
    avatarUrl = avatarUrl,
)

/** DB enum metni → domain rol; bilinmeyen değer güvenli varsayılan (BUYER). */
internal fun String.toUserRole(): UserRole = when (lowercase()) {
    "seller" -> UserRole.SELLER
    "admin" -> UserRole.ADMIN
    else -> UserRole.BUYER
}
