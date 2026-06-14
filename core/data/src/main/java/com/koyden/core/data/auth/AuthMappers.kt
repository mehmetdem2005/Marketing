package com.koyden.core.data.auth

import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.ErrorType
import com.koyden.core.domain.auth.AuthUser
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import java.io.IOException
import java.net.UnknownHostException

/** Supabase [UserInfo] → domain [AuthUser]. Görünen ad metadata'dan (varsa) okunur. */
internal fun UserInfo.toAuthUser(): AuthUser {
    val name = (userMetadata?.get("full_name") as? JsonPrimitive)?.contentOrNull
        ?: (userMetadata?.get("name") as? JsonPrimitive)?.contentOrNull
    return AuthUser(id = id, email = email, displayName = name)
}

/**
 * İstisna → [AppError]. **Ham mesaj/sağlayıcı detayı UI'a taşınmaz** (güvenlik/ISO 25010):
 * yalnız [ErrorType] belirlenir, mesaj null bırakılır (UI yerelleştirir). [cause] log için tutulur.
 */
internal fun Throwable.toAppError(): AppError {
    val msg = message.orEmpty()
    val type = when {
        this is UnknownHostException || this is IOException -> ErrorType.NETWORK
        contains(msg, "timeout") -> ErrorType.TIMEOUT
        contains(msg, "invalid login", "invalid_grant", "invalid credentials", "bad_credentials") -> ErrorType.UNAUTHORIZED
        contains(msg, "already registered", "user already", "already exists") -> ErrorType.CONFLICT
        contains(msg, "network", "unable to resolve host", "connect") -> ErrorType.NETWORK
        contains(msg, "not found") -> ErrorType.NOT_FOUND
        else -> ErrorType.UNKNOWN
    }
    return AppError(type = type, cause = this)
}

private fun contains(haystack: String, vararg needles: String): Boolean =
    needles.any { haystack.contains(it, ignoreCase = true) }
