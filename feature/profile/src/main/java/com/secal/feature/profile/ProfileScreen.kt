package com.secal.feature.profile

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.secal.core.domain.profile.Profile
import com.secal.core.domain.profile.UserRole
import com.secal.core.ui.state.UiStateScaffold
import com.secal.core.ui.state.errorMessage
import com.secal.designsystem.component.SecalButton
import com.secal.designsystem.component.SecalButtonVariant
import com.secal.designsystem.component.SecalCard
import com.secal.designsystem.component.SecalStatusBadge
import com.secal.designsystem.component.SecalTextField
import com.secal.designsystem.theme.LocalSpacing

/**
 * Profil ekranı: yüklemeyi 4-durum scaffold ile yönetir (skeleton/hata/içerik),
 * içerik içinde ad/telefon düzenleme + kaydet + oturum kapatma. Tüm geçişler reduce-motion'a
 * saygılı (UiStateScaffold crossfade + buton press micro-etkileşimi).
 */
@Composable
fun ProfileScreen(
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.signedOut) {
        if (state.signedOut) onSignedOut()
    }
    UiStateScaffold(
        state = state.profile,
        modifier = modifier.fillMaxSize(),
        onRetry = viewModel::load,
    ) { profile ->
        ProfileContent(
            profile = profile,
            state = state,
            onFullNameChange = viewModel::onFullNameChange,
            onPhoneChange = viewModel::onPhoneChange,
            onSave = viewModel::save,
            onSignOut = viewModel::signOut,
        )
    }
}

@Composable
private fun ProfileContent(
    profile: Profile,
    state: ProfileUiState,
    onFullNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit,
    onSignOut: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val saving = state.saving
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text(
            text = "Profilim",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = spacing.xs),
        )

        // Kimlik kartı: rol rozeti + e-posta (salt-okunur; auth'tan gelir).
        SecalCard {
            SecalStatusBadge(text = roleLabel(profile.role))
            Text(
                text = profile.email ?: "E-posta yok",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = spacing.xs),
            )
        }

        SecalTextField(
            value = state.fullName,
            onValueChange = onFullNameChange,
            label = "Ad Soyad",
            enabled = !saving,
            modifier = Modifier.padding(top = spacing.sm),
        )
        SecalTextField(
            value = state.phone,
            onValueChange = onPhoneChange,
            label = "Telefon",
            keyboardType = KeyboardType.Phone,
            enabled = !saving,
        )

        SecalButton(
            text = "Kaydet",
            onClick = onSave,
            loading = saving,
            modifier = Modifier.fillMaxWidth().padding(top = spacing.sm),
        )

        if (state.saved) {
            Text(
                text = "Profilin güncellendi.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        if (state.saveError != null) {
            Text(
                text = errorMessage(state.saveError),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = spacing.sm))

        SecalButton(
            text = "Oturumu kapat",
            onClick = onSignOut,
            variant = SecalButtonVariant.Secondary,
            enabled = !saving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Domain rol → kullanıcıya gösterilecek Türkçe etiket. */
private fun roleLabel(role: UserRole): String = when (role) {
    UserRole.BUYER -> "Alıcı"
    UserRole.SELLER -> "Satıcı"
    UserRole.ADMIN -> "Yönetici"
}
