// Kök build dosyası — alt projeler kendi plugin'lerini version catalog'dan uygular.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
}

// --- detekt: tüm modüller için ortak kalite/a11y/güvenlik kapısı ---
// Not: version catalog accessor (`libs`) `subprojects {}` lambda'sının receiver'ında (alt proje)
// bulunmaz; bu yüzden detekt sürümünü blok DIŞINDA (kök scope) yakalayıp string olarak kullanırız.
val detektVersion = libs.versions.detekt.get()

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        parallel = true
        // ktlint formatting ihlalleri CI'da yerinde otomatik düzeltilir (yerelde formatter
        // çalıştırılamadığı için gate'i bloklamasın); düzeltilenler excludeCorrectable ile sayılmaz.
        autoCorrect = true
    }

    dependencies {
        add(
            "detektPlugins",
            "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion",
        )
    }
}
