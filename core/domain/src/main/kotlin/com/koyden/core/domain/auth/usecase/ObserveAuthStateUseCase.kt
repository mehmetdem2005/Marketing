package com.koyden.core.domain.auth.usecase

import com.koyden.core.domain.auth.AuthState
import com.koyden.core.domain.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Oturum durumu akışı (UI yönlendirmesi için). */
class ObserveAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthState> = repository.authState
}
