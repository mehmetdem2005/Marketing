package com.koyden.core.data.auth

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.auth.AuthRepository
import com.koyden.core.domain.auth.AuthState
import com.koyden.core.domain.auth.AuthUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AuthRepository] portunun Supabase auth-kt implementasyonu (data katmanı/adapter).
 * Hatalar [DataResult.Failure] ile taşınır; ham istisna UI'a sızmaz.
 */
@Singleton
class SupabaseAuthRepository @Inject constructor(
    private val client: SupabaseClient,
) : AuthRepository {

    private val auth get() = client.auth

    override val authState: Flow<AuthState> = auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Authenticated -> status.session.user
                ?.toAuthUser()?.let { AuthState.Authenticated(it) }
                ?: AuthState.Unauthenticated
            is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated
            else -> AuthState.Loading // Initializing / RefreshFailure
        }
    }

    override fun currentUser(): AuthUser? = auth.currentUserOrNull()?.toAuthUser()

    override suspend fun signUpWithEmail(email: String, password: String): DataResult<AuthUser> =
        runAuth {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            // E-posta doğrulaması kapalıysa oturum açılır; açıksa kullanıcı döner.
            auth.currentUserOrNull()?.toAuthUser() ?: missingUser()
        }

    override suspend fun signInWithEmail(email: String, password: String): DataResult<AuthUser> =
        runAuth {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            auth.currentUserOrNull()?.toAuthUser() ?: missingUser()
        }

    override suspend fun signInWithGoogle(idToken: String, rawNonce: String?): DataResult<AuthUser> =
        runAuth {
            auth.signInWith(IDToken) {
                this.idToken = idToken
                this.provider = Google
                this.nonce = rawNonce
            }
            auth.currentUserOrNull()?.toAuthUser() ?: missingUser()
        }

    override suspend fun sendPasswordReset(email: String): DataResult<Unit> =
        runAuth { auth.resetPasswordForEmail(email) }

    override suspend fun signOut(): DataResult<Unit> =
        runAuth { auth.signOut() }

    /** İptal istisnasını yutmadan, diğer hataları [AppError]'a eşleyen sarmalayıcı. */
    private suspend fun <T> runAuth(block: suspend () -> T): DataResult<T> =
        try {
            DataResult.Success(block())
        } catch (c: CancellationException) {
            throw c
        } catch (t: Throwable) {
            DataResult.Failure(t.toAppError())
        }

    private fun missingUser(): Nothing =
        error("Auth işlemi sonrası kullanıcı oturumu alınamadı")
}
