package com.koyden.core.domain.auth.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.auth.AuthRepository
import javax.inject.Inject

/** Oturumu kapatır. */
class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): DataResult<Unit> = repository.signOut()
}
