package com.koyden.core.domain.auth.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.auth.AuthRepository
import com.koyden.core.domain.auth.AuthUser
import com.koyden.core.domain.auth.CredentialValidation
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
