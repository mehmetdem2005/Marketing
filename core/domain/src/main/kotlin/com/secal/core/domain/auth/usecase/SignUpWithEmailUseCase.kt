package com.secal.core.domain.auth.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.auth.AuthRepository
import com.secal.core.domain.auth.AuthUser
import com.secal.core.domain.auth.CredentialValidation
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
                com.secal.core.common.result.AppError(
                    type = com.secal.core.common.result.ErrorType.VALIDATION,
                    message = "Şifreler eşleşmiyor.",
                ),
            )
        }
        return repository.signUpWithEmail(email.trim(), password)
    }
}
