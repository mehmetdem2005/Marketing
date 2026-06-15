package com.secal.core.domain.auth

import com.secal.core.common.result.DataResult
import kotlinx.coroutines.flow.Flow

/**
 * Kimlik doğrulama **portu** (domain). Data katmanı (Supabase auth-kt) implement eder.
 * Hatalar istisna yerine [DataResult.Failure] ile taşınır (öngörülebilir akış).
 */
interface AuthRepository {
    /** Oturum durumu akışı (uygulama açılışında ve değişimlerde). */
    val authState: Flow<AuthState>

    /** Anlık kullanıcı (senkron); oturum yoksa null. */
    fun currentUser(): AuthUser?

    suspend fun signUpWithEmail(email: String, password: String): DataResult<AuthUser>

    suspend fun signInWithEmail(email: String, password: String): DataResult<AuthUser>

    /**
     * Google ile giriş. [idToken] istemcide Credential Manager'dan alınır; [rawNonce]
     * (varsa) tekrar-saldırı (replay) korumasıdır — idToken'daki hash'li nonce ile eşleşir.
     */
    suspend fun signInWithGoogle(idToken: String, rawNonce: String?): DataResult<AuthUser>

    suspend fun sendPasswordReset(email: String): DataResult<Unit>

    suspend fun signOut(): DataResult<Unit>
}
