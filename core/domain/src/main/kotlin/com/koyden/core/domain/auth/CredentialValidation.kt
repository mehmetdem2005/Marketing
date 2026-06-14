package com.koyden.core.domain.auth

import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.ErrorType

/**
 * Girdi doğrulama (domain — ISO 29148 gereksinim, ISO 25010 güvenilirlik). Sunucuya
 * gitmeden istemcide ön doğrulama; sunucu (RLS/constraint) yine de son sözü söyler.
 * Hata varsa [AppError] (type=VALIDATION) döner, yoksa null.
 */
object CredentialValidation {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    const val MIN_PASSWORD_LENGTH = 8

    fun validateEmail(email: String): AppError? = when {
        email.isBlank() -> error("E-posta boş olamaz.")
        !EMAIL_REGEX.matches(email.trim()) -> error("Geçerli bir e-posta gir.")
        else -> null
    }

    fun validatePassword(password: String): AppError? = when {
        password.isEmpty() -> error("Şifre boş olamaz.")
        password.length < MIN_PASSWORD_LENGTH -> error("Şifre en az $MIN_PASSWORD_LENGTH karakter olmalı.")
        else -> null
    }

    private fun error(message: String) = AppError(type = ErrorType.VALIDATION, message = message)
}
