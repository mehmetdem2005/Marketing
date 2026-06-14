package com.koyden.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.ErrorType
import com.koyden.core.ui.state.errorMessage
import com.koyden.designsystem.component.KoydenButton
import com.koyden.designsystem.component.KoydenButtonVariant
import com.koyden.designsystem.component.KoydenTextField
import com.koyden.designsystem.theme.LocalSpacing
import com.koyden.feature.auth.google.GoogleCredentialClient
import kotlinx.coroutines.launch

/**
 * Giriş ekranı (e-posta/şifre + Google). 4-durum'un "submitting/error" alt-durumları
 * butonlarda ve hata metninde ele alınır.
 */
@Composable
fun LoginScreen(
    onSignedIn: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.signedIn) {
        if (state.signedIn) {
            onSignedIn()
            viewModel.consumeSignedIn()
        }
    }
    AuthFormLayout(
        title = "Giriş yap",
        modifier = modifier,
        error = state.error,
    ) { spacing ->
        KoydenTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = "E-posta",
            keyboardType = KeyboardType.Email,
            enabled = !state.submitting,
        )
        KoydenTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Şifre",
            isPassword = true,
            keyboardType = KeyboardType.Password,
            enabled = !state.submitting,
            modifier = Modifier.padding(top = spacing.sm),
        )
        KoydenButton(
            text = "Giriş yap",
            onClick = viewModel::login,
            loading = state.submitting,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.md),
        )
        GoogleSignInButton(viewModel = viewModel, modifier = Modifier.padding(top = spacing.sm))
        KoydenButton(
            text = "Hesabın yok mu? Kayıt ol",
            onClick = onNavigateToRegister,
            variant = KoydenButtonVariant.Text,
            enabled = !state.submitting,
            modifier = Modifier.padding(top = spacing.xs),
        )
    }
}

/** Kayıt ekranı (e-posta/şifre + şifre tekrarı + Google). */
@Composable
fun RegisterScreen(
    onSignedIn: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.signedIn) {
        if (state.signedIn) {
            onSignedIn()
            viewModel.consumeSignedIn()
        }
    }
    AuthFormLayout(
        title = "Kayıt ol",
        modifier = modifier,
        error = state.error,
    ) { spacing ->
        KoydenTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = "E-posta",
            keyboardType = KeyboardType.Email,
            enabled = !state.submitting,
        )
        KoydenTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Şifre (en az 8 karakter)",
            isPassword = true,
            keyboardType = KeyboardType.Password,
            enabled = !state.submitting,
            modifier = Modifier.padding(top = spacing.sm),
        )
        KoydenTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = "Şifre (tekrar)",
            isPassword = true,
            keyboardType = KeyboardType.Password,
            enabled = !state.submitting,
            modifier = Modifier.padding(top = spacing.sm),
        )
        KoydenButton(
            text = "Kayıt ol",
            onClick = viewModel::register,
            loading = state.submitting,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.md),
        )
        GoogleSignInButton(viewModel = viewModel, modifier = Modifier.padding(top = spacing.sm))
        KoydenButton(
            text = "Zaten hesabın var mı? Giriş yap",
            onClick = onNavigateToLogin,
            variant = KoydenButtonVariant.Text,
            enabled = !state.submitting,
            modifier = Modifier.padding(top = spacing.xs),
        )
    }
}

@Composable
private fun GoogleSignInButton(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    KoydenButton(
        text = "Google ile devam et",
        variant = KoydenButtonVariant.Secondary,
        modifier = modifier.fillMaxWidth(),
        onClick = {
            // Not: setSubmitting(true) ÇAĞIRMA — loginWithGoogle'daki submit guard'ını tetikler.
            // Sistem kimlik bilgisi diyaloğu modaldır; token alındıktan sonra submit() spinner'ı yönetir.
            scope.launch {
                GoogleCredentialClient(context)
                    .requestIdToken(viewModel.googleServerClientId)
                    .onSuccess { viewModel.loginWithGoogle(it.idToken, it.rawNonce) }
                    .onFailure {
                        viewModel.onGoogleFailure(
                            AppError(type = ErrorType.UNKNOWN, message = "Google ile giriş tamamlanamadı."),
                        )
                    }
            }
        },
    )
}

/** Ortak auth form yerleşimi: başlık + alanlar + hata metni. */
@Composable
private fun AuthFormLayout(
    title: String,
    error: AppError?,
    modifier: Modifier = Modifier,
    content: @Composable (spacing: com.koyden.designsystem.theme.Spacing) -> Unit,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = spacing.md),
        )
        content(spacing)
        if (error != null) {
            HorizontalDivider(modifier = Modifier.padding(vertical = spacing.sm))
            Text(
                text = errorMessage(error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }
    }
}
