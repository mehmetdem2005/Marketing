package com.secal.core.ui.state

import com.secal.core.common.result.AppError

/**
 * Ekran düzeyinde tek-yönlü durum (UDF / FSM) — **4-durum kuralı**.
 * Her veri ekranı bu dört durumu ele alır; UI yalnız `when` ile ayrıştırır.
 * docs/tasarim-sistemi.md "4-Durum Kuralı".
 */
sealed interface UiState<out T> {
    /** İlk yükleme / yenileme. UI skeleton (shimmer) gösterir. */
    data object Loading : UiState<Nothing>

    /** Başarılı ama boş sonuç (ör. arama 0 kayıt). UI EmptyState gösterir. */
    data object Empty : UiState<Nothing>

    /** Hata. UI ErrorState + (varsa) yeniden dene gösterir. */
    data class Error(val error: AppError) : UiState<Nothing>

    /** İçerik mevcut. */
    data class Content<T>(val data: T) : UiState<T>
}

/** Koleksiyon sonucu boşsa [UiState.Empty], doluysa [UiState.Content]'e eşler. */
fun <T> List<T>.toUiState(): UiState<List<T>> =
    if (isEmpty()) UiState.Empty else UiState.Content(this)
