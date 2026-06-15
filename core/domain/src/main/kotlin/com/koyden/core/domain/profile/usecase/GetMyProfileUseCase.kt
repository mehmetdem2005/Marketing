package com.koyden.core.domain.profile.usecase

import com.koyden.core.common.result.DataResult
import com.koyden.core.domain.profile.Profile
import com.koyden.core.domain.profile.ProfileRepository
import javax.inject.Inject

/** Oturumdaki kullanıcının profilini getirir. */
class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): DataResult<Profile> = repository.getMyProfile()
}
