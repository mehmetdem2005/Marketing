package com.secal.core.domain.auth.usecase

import com.secal.core.domain.auth.AuthState
import com.secal.core.domain.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Oturum durumu akışı (UI yönlendirmesi için). */
class ObserveAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthState> = repository.authState
}
