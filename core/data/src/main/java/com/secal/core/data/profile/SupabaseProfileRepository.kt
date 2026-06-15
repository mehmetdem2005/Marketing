package com.secal.core.data.profile

import com.secal.core.common.result.AppError
import com.secal.core.common.result.DataResult
import com.secal.core.common.result.ErrorType
import com.secal.core.data.auth.toAppError
import com.secal.core.domain.profile.Profile
import com.secal.core.domain.profile.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [ProfileRepository] portunun Supabase Postgrest implementasyonu (RLS self read/update).
 * Kullanıcı id/e-posta auth oturumundan alınır; satır RLS ile yalnız sahibine açıktır.
 */
@Singleton
class SupabaseProfileRepository @Inject constructor(
    private val client: SupabaseClient,
) : ProfileRepository {

    private val table get() = client.postgrest.from(TABLE)

    override suspend fun getMyProfile(): DataResult<Profile> = runResult {
        val user = currentUserOrThrow()
        val dto = table.select(Columns.list("id", "role", "full_name", "phone", "avatar_url")) {
            filter { eq("id", user.id) }
        }.decodeSingle<ProfileDto>()
        dto.toDomain(email = user.email)
    }

    override suspend fun updateMyProfile(fullName: String?, phone: String?): DataResult<Profile> =
        runResult {
            val user = currentUserOrThrow()
            table.update(ProfileUpdateDto(fullName = fullName, phone = phone)) {
                filter { eq("id", user.id) }
            }
            // Güncelden sonra taze satırı oku (tek doğruluk kaynağı DB).
            val dto = table.select(Columns.list("id", "role", "full_name", "phone", "avatar_url")) {
                filter { eq("id", user.id) }
            }.decodeSingle<ProfileDto>()
            dto.toDomain(email = user.email)
        }

    private fun currentUserOrThrow() =
        client.auth.currentUserOrNull()
            ?: error("Oturum bulunamadı")

    private inline fun <T> runResult(block: () -> T): DataResult<T> =
        try {
            DataResult.Success(block())
        } catch (c: CancellationException) {
            throw c
        } catch (e: IllegalStateException) {
            DataResult.Failure(AppError(type = ErrorType.UNAUTHORIZED, cause = e))
        } catch (t: Throwable) {
            DataResult.Failure(t.toAppError())
        }

    private companion object {
        const val TABLE = "profiles"
    }
}
