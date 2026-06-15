package com.secal.core.domain.auth.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.auth.AuthRepository
import com.secal.core.domain.auth.AuthUser
import com.secal.core.domain.auth.CredentialValidation
import javax.inject.Inject

/** E-posta + şifre ile giriş. Önce istemci doğrulaması, sonra port çağrısı. */
class SignInWithEmailUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): DataResult<AuthUser> {
        CredentialValidation.validateEmail(email)?.let { return DataResult.Failure(it) }
        CredentialValidation.validatePassword(password)?.let { return DataResult.Failure(it) }
        return repository.signInWithEmail(email.trim(), password)
    }
}
