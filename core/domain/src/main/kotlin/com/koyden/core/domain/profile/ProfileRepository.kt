package com.koyden.core.domain.profile

import com.koyden.core.common.result.DataResult

/**
 * Profil **portu** (domain). Data katmanı (Supabase Postgrest + RLS) implement eder.
 * Yalnız oturum açmış kullanıcının kendi profili (RLS self read/update — `docs/guvenlik.md`).
 */
interface ProfileRepository {
    /** Oturumdaki kullanıcının profilini getirir. */
    suspend fun getMyProfile(): DataResult<Profile>

    /**
     * Kendi profilini günceller (yalnız ad/telefon). Güncellenmiş profili döner.
     * Rol ve id istemciden değiştirilemez.
     */
    suspend fun updateMyProfile(fullName: String?, phone: String?): DataResult<Profile>
}
