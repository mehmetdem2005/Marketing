package com.secal.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secal.core.common.result.AppError
import com.secal.core.common.result.DataResult
import com.secal.core.domain.profile.Profile
import com.secal.core.domain.profile.usecase.GetMyProfileUseCase
import com.secal.core.domain.profile.usecase.UpdateProfileUseCase
import com.secal.core.domain.auth.usecase.SignOutUseCase
import com.secal.core.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Profil ekranı durumu (tek-yönlü). [profile] yüklemenin 4-durumunu taşır; düzenleme
 * alanları ([fullName]/[phone]) içerikten ön-doldurulur. Kaydetme alt-durumu ayrı tutulur
 * (içerik görünür kalırken inline geri bildirim — progressive disclosure).
 */
data class ProfileUiState(
    val profile: UiState<Profile> = UiState.Loading,
    val fullName: String = "",
    val phone: String = "",
    val saving: Boolean = false,
    val saved: Boolean = false,
    val saveError: AppError? = null,
    val signedOut: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyProfile: GetMyProfileUseCase,
    private val updateProfile: UpdateProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(profile = UiState.Loading, saveError = null, saved = false) }
        viewModelScope.launch {
            when (val result = getMyProfile()) {
                is DataResult.Success -> _state.update {
                    it.copy(
                        profile = UiState.Content(result.data),
                        fullName = result.data.fullName.orEmpty(),
                        phone = result.data.phone.orEmpty(),
                    )
                }
                is DataResult.Failure -> _state.update { it.copy(profile = UiState.Error(result.error)) }
            }
        }
    }

    fun onFullNameChange(value: String) =
        _state.update { it.copy(fullName = value, saved = false, saveError = null) }

    fun onPhoneChange(value: String) =
        _state.update { it.copy(phone = value, saved = false, saveError = null) }

    fun consumeSaved() = _state.update { it.copy(saved = false) }

    fun save() {
        val current = _state.value
        if (current.saving) return
        _state.update { it.copy(saving = true, saveError = null, saved = false) }
        viewModelScope.launch {
            when (val result = updateProfile(current.fullName, current.phone)) {
                is DataResult.Success -> _state.update {
                    it.copy(
                        saving = false,
                        saved = true,
                        profile = UiState.Content(result.data),
                        fullName = result.data.fullName.orEmpty(),
                        phone = result.data.phone.orEmpty(),
                    )
                }
                is DataResult.Failure -> _state.update { it.copy(saving = false, saveError = result.error) }
            }
        }
    }

    fun signOut() {
        if (_state.value.saving) return
        viewModelScope.launch {
            when (val result = signOutUseCase()) {
                is DataResult.Success -> _state.update { it.copy(signedOut = true) }
                is DataResult.Failure -> _state.update { it.copy(saveError = result.error) }
            }
        }
    }
}
