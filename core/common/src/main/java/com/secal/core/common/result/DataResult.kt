package com.secal.core.common.result

/**
 * Katmanlar arası tek-yönlü sonuç tipi. Domain/data katmanı bu tipi döndürür;
 * UI katmanı `when` ile dört durumu (loading hariç) ayrıştırır.
 *
 * Hatalar istisna fırlatmak yerine [Failure] ile taşınır (öngörülebilir akış).
 */
sealed interface DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>
    data class Failure(val error: AppError) : DataResult<Nothing>
}

inline fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> = when (this) {
    is DataResult.Success -> DataResult.Success(transform(data))
    is DataResult.Failure -> this
}

inline fun <T> DataResult<T>.onSuccess(action: (T) -> Unit): DataResult<T> {
    if (this is DataResult.Success) action(data)
    return this
}

inline fun <T> DataResult<T>.onFailure(action: (AppError) -> Unit): DataResult<T> {
    if (this is DataResult.Failure) action(error)
    return this
}
