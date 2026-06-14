package com.koyden.core.domain.auth.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.auth.AuthRepository
import com.koyden.core.domain.auth.AuthUser
import com.koyden.core.domain.auth.CredentialValidation
import javax.inject.Inject

/** E-posta + şifre ile kayıt. Doğrulama + şifre tekrarı eşleşmesi. */
class SignUpWithEmailUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): DataResult<AuthUser> {
        CredentialValidation.validateEmail(email)?.let { return DataResult.Failure(it) }
        CredentialValidation.validatePassword(password)?.let { return DataResult.Failure(it) }
        if (password != confirmPassword) {
            return DataResult.Failure(
                com.koyden.core.common.result.AppError(
                    type = com.koyden.core.common.result.ErrorType.VALIDATION,
                    message = "Şifreler eşleşmiyor.",
                ),
            )
        }
        return repository.signUpWithEmail(email.trim(), password)
    }
}
