package com.koyden.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.DataResult
import com.koyden.core.data.auth.AuthConfig
import com.koyden.core.domain.auth.usecase.SignInWithEmailUseCase
import com.koyden.core.domain.auth.usecase.SignInWithGoogleUseCase
import com.koyden.core.domain.auth.usecase.SignUpWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Giriş/kayıt ekranı durumu (tek-yönlü). */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val submitting: Boolean = false,
    val error: AppError? = null,
    /** Başarılı giriş/kayıt → ekran ana akışa yönlendirir, sonra [consumeSignedIn]. */
    val signedIn: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithEmail: SignInWithEmailUseCase,
    private val signUpWithEmail: SignUpWithEmailUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    authConfig: AuthConfig,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    /** Google "ile giriş" için sunucu (Web) istemci kimliği. */
    val googleServerClientId: String = authConfig.googleServerClientId

    fun onEmailChange(value: String) = _state.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String) = _state.update { it.copy(password = value, error = null) }
    fun onConfirmPasswordChange(value: String) = _state.update { it.copy(confirmPassword = value, error = null) }
    fun consumeSignedIn() = _state.update { it.copy(signedIn = false) }

    fun login() = submit {
        signInWithEmail(_state.value.email, _state.value.password)
    }

    fun register() = submit {
        signUpWithEmail(_state.value.email, _state.value.password, _state.value.confirmPassword)
    }

    /** Ekran Credential Manager'dan token alıp çağırır. */
    fun loginWithGoogle(idToken: String, rawNonce: String?) = submit {
        signInWithGoogle(idToken, rawNonce)
    }

    /** Google akışı istemci tarafında başarısız olursa (iptal/cihaz) hata göster. */
    fun onGoogleFailure(error: AppError) = _state.update { it.copy(submitting = false, error = error) }

    private fun submit(action: suspend () -> DataResult<*>) {
        if (_state.value.submitting) return
        _state.update { it.copy(submitting = true, error = null) }
        viewModelScope.launch {
            when (val result = action()) {
                is DataResult.Success -> _state.update { it.copy(submitting = false, signedIn = true) }
                is DataResult.Failure -> _state.update { it.copy(submitting = false, error = result.error) }
            }
        }
    }
}
