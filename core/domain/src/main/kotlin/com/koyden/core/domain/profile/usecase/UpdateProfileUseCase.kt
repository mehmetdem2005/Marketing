package com.koyden.core.domain.profile.usecase

import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.DataResult
import com.koyden.core.common.result.ErrorType
import com.koyden.core.domain.profile.Profile
import com.koyden.core.domain.profile.ProfileRepository
import com.koyden.core.domain.profile.ProfileValidation
import javax.inject.Inject

/**
 * Profil günceller. Ham ad/telefon normalize + doğrulanır; geçersizse ağ çağrısı YAPILMADAN
 * [DataResult.Failure] (VALIDATION) döner (öngörülebilir akış, gereksiz istek yok).
 */
class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(rawFullName: String, rawPhone: String): DataResult<Profile> {
        if (!ProfileValidation.isFullNameValid(rawFullName)) {
            return DataResult.Failure(
                AppError(
                    type = ErrorType.VALIDATION,
                    message = "Ad en fazla ${ProfileValidation.FULL_NAME_MAX} karakter olabilir.",
                ),
            )
        }
        return repository.updateMyProfile(
            fullName = ProfileValidation.normalizeFullName(rawFullName),
            phone = ProfileValidation.normalizePhone(rawPhone),
        )
    }
}
