package com.koyden.core.ui.state

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.koyden.core.common.result.AppError
import com.koyden.core.common.result.ErrorType
import com.koyden.designsystem.component.EmptyState
import com.koyden.designsystem.component.ErrorState
import com.koyden.designsystem.component.SkeletonLine
import com.koyden.designsystem.motion.MotionTokens
import com.koyden.designsystem.motion.motionDuration

/**
 * **4-durum kuralı** scaffold'u: [UiState]'i loading/empty/error/content görünümlerine
 * bağlar. Durumlar arası geçiş reduce-motion'a saygılı crossfade (motion-design).
 * Varsayılan loading = skeleton, empty/error = designsystem durum görünümleri;
 * çağıran slot'larla özelleştirebilir.
 */
@Composable
fun <T> UiStateScaffold(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    loading: @Composable () -> Unit = { DefaultSkeleton() },
    empty: @Composable () -> Unit = { EmptyState(title = "Henüz içerik yok") },
    content: @Composable (T) -> Unit,
) {
    val d = motionDuration(MotionTokens.DurationStandard)
    AnimatedContent(
        targetState = state,
        modifier = modifier,
        transitionSpec = { fadeIn(tween(d)) togetherWith fadeOut(tween(d)) },
        contentKey = { it.key() },
        label = "uiState",
    ) { current ->
        when (current) {
            is UiState.Loading -> loading()
            is UiState.Empty -> empty()
            is UiState.Error -> ErrorState(message = errorMessage(current.error), onRetry = onRetry)
            is UiState.Content -> content(current.data)
        }
    }
}

/** AnimatedContent'in aynı durum tipinde gereksiz animasyon yapmaması için anahtar. */
private fun UiState<*>.key(): Int = when (this) {
    is UiState.Loading -> 0
    is UiState.Empty -> 1
    is UiState.Error -> 2
    is UiState.Content -> 3
}

@Composable
private fun DefaultSkeleton() {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        repeat(6) {
            SkeletonLine(modifier = Modifier.padding(vertical = 6.dp), height = 20.dp)
        }
    }
}

/**
 * [AppError] → kullanıcıya gösterilecek **yerelleştirilmiş** mesaj. Ham istisna/sağlayıcı
 * detayı sızdırılmaz (güvenlik / ISO 25010). Özel mesaj verilmişse o tercih edilir.
 */
fun errorMessage(error: AppError): String = error.message ?: when (error.type) {
    ErrorType.NETWORK -> "İnternet bağlantısı yok. Bağlantını kontrol edip tekrar dene."
    ErrorType.TIMEOUT -> "İstek zaman aşımına uğradı. Lütfen tekrar dene."
    ErrorType.UNAUTHORIZED -> "Oturumun sona ermiş. Lütfen tekrar giriş yap."
    ErrorType.FORBIDDEN -> "Bu işlem için yetkin yok."
    ErrorType.NOT_FOUND -> "Aradığın içerik bulunamadı."
    ErrorType.VALIDATION -> "Girilen bilgileri kontrol et."
    ErrorType.CONFLICT -> "İşlem çakıştı. Sayfayı yenileyip tekrar dene."
    ErrorType.SERVER -> "Sunucuda bir sorun oluştu. Birazdan tekrar dene."
    ErrorType.UNKNOWN -> "Beklenmeyen bir hata oluştu. Lütfen tekrar dene."
}
