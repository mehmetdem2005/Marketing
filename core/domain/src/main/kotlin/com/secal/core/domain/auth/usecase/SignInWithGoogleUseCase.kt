package com.secal.core.domain.auth.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.auth.AuthRepository
import com.secal.core.domain.auth.AuthUser
import javax.inject.Inject

/** Google ID token ile giriş (token istemcide Credential Manager'dan alınır). */
class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String, rawNonce: String?): DataResult<AuthUser> =
        repository.signInWithGoogle(idToken, rawNonce)
}
