package com.secal.core.domain.auth.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.auth.AuthRepository
import javax.inject.Inject

/** Oturumu kapatır. */
class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): DataResult<Unit> = repository.signOut()
}
