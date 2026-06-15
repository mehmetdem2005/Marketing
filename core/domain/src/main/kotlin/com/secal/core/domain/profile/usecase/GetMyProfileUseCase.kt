package com.secal.core.domain.profile.usecase

import com.secal.core.common.result.DataResult
import com.secal.core.domain.profile.Profile
import com.secal.core.domain.profile.ProfileRepository
import javax.inject.Inject

/** Oturumdaki kullanıcının profilini getirir. */
class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): DataResult<Profile> = repository.getMyProfile()
}
