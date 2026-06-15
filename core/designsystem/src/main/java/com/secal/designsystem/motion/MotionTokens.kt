package com.secal.designsystem.motion

import android.provider.Settings
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * SeçAl hareket (motion) token'ları — süre/easing **tek kaynak**; ekranlarda sabit ms YASAK.
 * Marka motion kişiliği: "Corporate + sıcak" (temiz, kararlı; düşük/sıfır overshoot).
 * Kaynak: motion-design skill (duration & easing tabloları) + docs/tasarim-sistemi.md.
 *
 * Süre paleti (motion-design duration table eşlemesi):
 *  - [DurationQuick]      buton press / toggle / badge / micro-feedback
 *  - [DurationStandard]   kart enter-exit / içerik (4-durum) geçişi
 *  - [DurationEmphasized] modal / bottom sheet / vurgulu giriş
 *  - [DurationLarge]      sayfa / bağlam geçişi
 */
object MotionTokens {
    const val DurationQuick = 150
    const val DurationStandard = 250
    const val DurationEmphasized = 350
    const val DurationLarge = 450

    /** Liste/grid micro-cascade adımı; toplam stagger < 500ms tutulur. */
    const val StaggerStepMs = 40

    // Easing paleti — Material 3 eğrileri (lineer YASAK; yalnız spinner/progress lineer olabilir)
    val EasingStandard: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)        // ekran içi
    val EasingEmphasized: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f) // giriş / dikkat
    val EasingAccelerate: Easing = CubicBezierEasing(0.3f, 0f, 1f, 1f)      // çıkış / kapanış
}

/**
 * Sistemin "animasyonları kaldır" tercihi (Settings.Global.ANIMATOR_DURATION_SCALE == 0).
 * true ise hareket süreleri 0'a indirilir / sade fade'e düşülür — WCAG 2.2 (2.3.3 / 2.2.2),
 * ISO 9241-110. Animasyon **varsayılan açık**, ama kullanıcı kapatabilir.
 */
@Composable
fun rememberReduceMotion(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        val scale = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        )
        scale == 0f
    }
}

/** reduce-motion'a saygılı süre: tercih kapalıysa 0, açıksa [durationMs]. */
@Composable
fun motionDuration(durationMs: Int): Int = if (rememberReduceMotion()) 0 else durationMs
