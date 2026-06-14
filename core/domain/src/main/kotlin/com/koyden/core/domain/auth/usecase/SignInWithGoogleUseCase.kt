package com.koyden.core.domain.auth.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.auth.AuthRepository
import com.koyden.core.domain.auth.AuthUser
import javax.inject.Inject

/** Google ID token ile giriş (token istemcide Credential Manager'dan alınır). */
class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String, rawNonce: String?): DataResult<AuthUser> =
        repository.signInWithGoogle(idToken, rawNonce)
}
